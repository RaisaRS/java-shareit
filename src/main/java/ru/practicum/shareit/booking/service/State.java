package ru.practicum.shareit.booking.service;

public enum State { //статус бронирования используется только в контроллере!!!!!
    CURRENT, //текущие
    PAST,   //прошлые
    FUTURE,  //будущие
    APPROVED, //подтверждённые
    WAITING,  //ожидают подтверждения
    REJECTED  //отклонённые
}
