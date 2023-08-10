package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE upper(i.available)" +
            " LIKE upper('true') AND (upper(i.name) LIKE upper(CONCAT('%', ?1,'%')) " +
            "OR upper(i.description) LIKE upper(CONCAT('%', ?1,'%')))")
    List<Item> searchItem(String text, Pageable pageable);

}
