package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;


/**
 * TODO Sprint add-controllers.
 */
@Entity
@Data
@Getter
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "is_available")
    private Boolean available;
//    @JoinColumn(name = "request", nullable = false)
//    @ToString.Exclude
//    private ItemRequest request;
}
