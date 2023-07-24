package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.Status;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.CommentNotAuthorNotBookingException;
import ru.practicum.shareit.exceptions.InCorrectBookingException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDtoShort;
import static ru.practicum.shareit.comment.dto.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Item saveItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item item = toItem(user, itemDto);
        log.info("Добавлен предмет {}, владелец: id = {}", itemDto, userId);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден " + userId));
        Item itemUpdate = itemRepository.findById(itemDto.getId()).orElseThrow(()->
                new ItemNotFoundException("Предмет не найден " + itemDto.getId()));
        Item item = toItem(user, itemDto);
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
    public ItemDto getItemById(long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Предмет не найден " + itemId));
        List<Comment> comments= commentRepository.findAllByItemId(itemId);
        ItemDto itemDto = toItemDto(item);
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
    public List<ItemDto> getItemsByUser(long userId) {
        if(!userRepository.existsById(userId)) {
            log.debug("Пользователь {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден " + userId);
        }
        if(itemRepository.findAllByOwnerId(userId) == null) {
            log.info("У пользователя {} нет предметов для аренды ", userId);
            throw new ItemNotFoundException("У пользователя нет предметов для аренды " + userId);
        }
        List<ItemDto> itemsList = itemRepository.findAllByOwnerId(userId)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList());
        itemsList.forEach(this::addLastAndNextDateTimeForBookingToItem);
        log.info("Список всех предметов, принадлежащих пользователю, id = {}", userId);
        return itemsList;
    }

    @Override
    public Collection<Item> searchItem(String text) {
        if(text.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("Выполнен поиск среди предметов по : {}.", text);
        return itemRepository.searchItem(text);
    }

    @Override
    public CommentDto postComment(long userId, Long itemId, CommentDto commentDto) {
        if(commentDto.getText().isEmpty()) {
            log.debug("Комментарий не может быть пустьм");
            throw new InCorrectBookingException("Комментарий не может быть пустьм");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("Пользователь не найден " + userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ItemNotFoundException("Предмет не найден " + itemId));

        LocalDateTime saveTime = LocalDateTime.now();

        Comment comment = CommentMapper.toComment(user, item, commentDto, saveTime);

        Booking booking = bookingRepository
                .findFirstBookingByItemIdAndEndIsBeforeAndStatusNotLikeOrderByEndDesc(itemId, saveTime,
                        Status.REJECTED);

        if(booking == null) {
            log.debug("Предмет {} ещё не бронировался, комментарии недоступны ", itemId);
            throw new InCorrectBookingException("Предмет ещё не бронировался, комментарии недоступны "
                    + itemId);
        }

        if(booking.getBooker().getId() != userId) {
            log.debug("Пользователь {} ранее не бронировал предмет {}, комментарии недоступны ", userId, itemId);
            throw new CommentNotAuthorNotBookingException("Пользователь ранее не бронировал предмет, " +
                    "комментарии недоступны " + userId + itemId);
        }
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(saveTime);

        commentRepository.save(comment);

        return toCommentDto(comment);
    }

    @Override
    public void deleteItemById(long userId, Long itemId) {
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

        if(next != null) {
            itemDto.setNextBooking(toBookingDtoShort(next));
        } else {
            itemDto.setNextBooking(null);
        }

        if(last != null) {
            itemDto.setLastBooking(toBookingDtoShort(last));
        } else {
            itemDto.setLastBooking(null);
        }
    }
}
