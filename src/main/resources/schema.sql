create table IF NOT EXISTS USERS
(
    USER_ID   BIGINT auto_increment,
    USER_NAME CHARACTER VARYING(255) not null,
    EMAIL     CHARACTER VARYING(512) not null
        constraint "USERS_pk2"
            unique,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table IF NOT EXISTS REQUESTS
(
    REQUEST_ID   BIGINT auto_increment,
    DESCRIPTION  VARCHAR(1000)               not null,
    REQUESTOR_ID BIGINT                      not null,
    CREATED      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint "REQUESTS_pk"
        primary key (REQUEST_ID),
    constraint "REQUESTS_USERS_ID_fk"
        foreign key (REQUESTOR_ID) references USERS (USER_ID) ON DELETE CASCADE
);

create table IF NOT EXISTS ITEMS
(
    ITEM_ID         BIGINT auto_increment,
    ITEM_NAME       VARCHAR(255)  not null,
    DESCRIPTION     VARCHAR(1012) not null,
    IS_AVAILABLE    BOOLEAN,
    OWNER_ID        BIGINT        not null,
    REQUEST         BIGINT,
    REQUEST_ITEM_ID BIGINT,
    constraint "ITEMS_pk"
        primary key (ITEM_ID),
    constraint "ITEMS_USERS_ID_fk"
        foreign key (OWNER_ID) references USERS (USER_ID) ON DELETE CASCADE,
    constraint "REQUEST_ITEM_ID_fk"
        foreign key (REQUEST_ITEM_ID) references REQUESTS (REQUEST_ID) ON DELETE CASCADE
);

create table IF NOT EXISTS BOOKINGS
(
    BOOKING_ID BIGINT auto_increment,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ITEM_ID    BIGINT                      not null,
    BOOKER_ID  BIGINT                      not null,
    STATUS     VARCHAR(8)                  not null,
    constraint "BOOKINGS_pk"
        primary key (BOOKING_ID),
    constraint "BOOKINGS_ITEMS_ID_fk"
        foreign key (ITEM_ID) references ITEMS (ITEM_ID) ON DELETE CASCADE,
    constraint "BOOKINGS_USERS_ID_fk"
        foreign key (BOOKER_ID) references USERS (USER_ID) ON DELETE CASCADE
);


create table IF NOT EXISTS COMMENTS
(
    COMMENT_ID BIGINT auto_increment,
    TEXT       CHARACTER VARYING(1000)     not null,
    ITEM_ID    BIGINT                      not null,
    AUTHOR_ID  BIGINT                      not null,
    CREATED    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint "COMMENTS_pk"
        primary key (COMMENT_ID),
    constraint "COMMENTS_ITEMS_ID_fk"
        foreign key (ITEM_ID) references ITEMS (ITEM_ID) ON DELETE CASCADE,
    constraint "COMMENTS_USERS_ID_fk"
        foreign key (AUTHOR_ID) references USERS (USER_ID) ON DELETE CASCADE
);
--DROP TABLE IF EXISTS users, items, bookings, requests, comments;