package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.MethodArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mappers.BookingMapper;
import ru.practicum.shareit.mappers.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.comment.dto.CommentMapper.toComment;
import static ru.practicum.shareit.item.comment.dto.CommentMapper.toCommentDto;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    @SneakyThrows
    public Item saveItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = ItemMapper.toItem(user, itemDto);
        log.info("Добавлен предмет {}, владелец: id = {}", itemDto, userId);
        item.setOwnerId(userId);
        item.setRequest(itemDto.getRequestId());

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item itemUpdate = itemRepository.findById(itemDto.getId()).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден " + itemDto.getId()));
        Item item = ItemMapper.toItem(user, itemDto);
        itemUpdate.setOwnerId(userId);
        if (Objects.nonNull(item.getName())) {
            itemUpdate.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription())) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable())) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        log.info("Выполнено обновление информации о предмете = {}, " +
                "принадлежащем пользователю, id = {}", item.getId(), userId);
        return itemRepository.save(itemUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден " + itemId));
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (item.getOwnerId().equals(userId)) {
            addLastAndNextDateTimeForBookingToItem(itemDto);
        }
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        log.info("Получен предмет, id = {}", itemId);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUser(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }

        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new MethodArgumentNotValidException(HttpStatus.BAD_REQUEST, "Неправильный параметр пагинации");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (itemRepository.findAllByOwnerIdOrderById(userId, pageable) == null) {
            log.info("У пользователя {} нет предметов для аренды ", userId);
            throw new ItemNotFoundException("У пользователя нет предметов для аренды " + userId);
        }

        List<ItemDto> itemsList = itemRepository.findAllByOwnerIdOrderById(userId, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        itemsList.forEach(this::addLastAndNextDateTimeForBookingToItem);

        log.info("Список всех предметов, принадлежащих пользователю, id = {}", userId);
        return itemsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> searchItem(String text, int from, int size) {
        if (text.isEmpty()) {
            log.debug("Запрос не найден");
            return Collections.emptyList();
        }
        String textToLowerCase = text.toLowerCase();
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new MethodArgumentNotValidException(HttpStatus.BAD_REQUEST, "Неправильный параметр пагинации");
        }
        Pageable pageable;
        if (text == null || text.isEmpty()) {
            log.debug("Предмет по запросу {} не найден", text);
            return Collections.emptyList();
        } else {
            pageable = PageRequest.of(from / size, size);
        }
        log.info("Выполнен поиск среди предметов по : {}.", text);
        return itemRepository
                .findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(textToLowerCase,
                        textToLowerCase, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto postComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            log.debug("Комментарий не может быть пустьм");
            throw new MethodArgumentNotValidException(HttpStatus.BAD_REQUEST, "Комментарий не может быть пустым");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "комментарий  к предмету с id = '" + itemId
                        + "' пользователем с id = " + userId + " ; нет информации о пользователе."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "комментарий к предмету с id = '" + itemId
                        + "' пользователем с id = '" + userId + "' - отсутствует запись о вещи."));

        List<Booking> bookings = item.getBookings();

        if (bookings
                .stream()
                .filter(booking -> (booking.getBooker().getId().equals(userId))
                        && !booking.getStart().isAfter(LocalDateTime.now())
                        && !booking.getStatus().equals(Status.REJECTED)
                )
                .collect(Collectors.toList()).size() == 0) {
            throw new MethodArgumentNotValidException(HttpStatus.BAD_REQUEST,
                    "Комментировать может только арендатор предмета, с наступившим началом времени бронирования " +
                            "и статусом НЕ REJECTED");
        }

        Comment comment = toComment(user, item, commentDto, commentDto.getCreated());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteItemById(Long userId, Long itemId) {
        userRepository.findById(userId);
        itemRepository.deleteById(itemId);
        log.info("Удален предмет {}, принадлежащий пользователю {}", itemId, userId);
    }

    private void addLastAndNextDateTimeForBookingToItem(ItemDto itemDto) {
        LocalDateTime timeNow = LocalDateTime.now();

        Booking next = bookingRepository
                .findFirstBookingByItemIdAndStartIsAfterAndStatusNotLikeOrderByStartAsc(itemDto.getId(),
                        timeNow, Status.REJECTED);
        Booking last = bookingRepository
                .findFirstBookingByItemIdAndStartIsBeforeAndStatusNotLikeOrderByStartDesc(itemDto.getId(),
                        timeNow, Status.REJECTED);

        if (next != null) {
            itemDto.setNextBooking(BookingMapper.toBookingDtoShort(next));
        } else {
            itemDto.setNextBooking(null);
        }

        if (last != null) {
            itemDto.setLastBooking(BookingMapper.toBookingDtoShort(last));
        } else {
            itemDto.setLastBooking(null);
        }
    }
}
