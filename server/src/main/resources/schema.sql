create table IF NOT EXISTS USERS
(
    USER_ID    SERIAL PRIMARY KEY,
    USER_NAME CHARACTER VARYING(255) not null,
    EMAIL     CHARACTER VARYING(512) not null
        constraint "USERS_pk2"
            unique
);

create table IF NOT EXISTS REQUESTS
(
    REQUEST_ID    SERIAL PRIMARY KEY,
    DESCRIPTION  VARCHAR(1000)               not null,
    REQUESTOR_ID BIGINT                      not null,
    CREATED      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint "REQUESTS_USERS_ID_fk"
        foreign key (REQUESTOR_ID) references USERS (USER_ID) ON DELETE CASCADE
);

create table IF NOT EXISTS ITEMS
(
    ITEM_ID          SERIAL PRIMARY KEY,
    ITEM_NAME       VARCHAR(255)  not null,
    DESCRIPTION     VARCHAR(1012) not null,
    IS_AVAILABLE    BOOLEAN,
    OWNER_ID        BIGINT        not null,
    REQUEST         BIGINT,
    REQUEST_ITEM_ID BIGINT,
    constraint "ITEMS_USERS_ID_fk"
        foreign key (OWNER_ID) references USERS (USER_ID) ON DELETE CASCADE,
    constraint "REQUEST_ITEM_ID_fk"
        foreign key (REQUEST_ITEM_ID) references REQUESTS (REQUEST_ID) ON DELETE CASCADE
);

create table IF NOT EXISTS BOOKINGS
(
    BOOKING_ID  SERIAL PRIMARY KEY,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ITEM_ID    BIGINT                      not null,
    BOOKER_ID  BIGINT                      not null,
    STATUS     VARCHAR(8)                  not null,
    constraint "BOOKINGS_ITEMS_ID_fk"
        foreign key (ITEM_ID) references ITEMS (ITEM_ID) ON DELETE CASCADE,
    constraint "BOOKINGS_USERS_ID_fk"
        foreign key (BOOKER_ID) references USERS (USER_ID) ON DELETE CASCADE
);


create table IF NOT EXISTS COMMENTS
(
    COMMENT_ID  SERIAL PRIMARY KEY,
    TEXT       CHARACTER VARYING(1000)     not null,
    ITEM_ID    BIGINT                      not null,
    AUTHOR_ID  BIGINT                      not null,
    CREATED    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    constraint "COMMENTS_ITEMS_ID_fk"
        foreign key (ITEM_ID) references ITEMS (ITEM_ID) ON DELETE CASCADE,
    constraint "COMMENTS_USERS_ID_fk"
        foreign key (AUTHOR_ID) references USERS (USER_ID) ON DELETE CASCADE
);
--DROP TABLE IF EXISTS users, items, bookings, requests, comments;