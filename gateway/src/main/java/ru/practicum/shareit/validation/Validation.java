package ru.practicum.shareit.validation;

import javax.validation.groups.Default;

public interface Validation {
    interface Post extends Default {
    }

    interface Patch extends Default {
    }
}