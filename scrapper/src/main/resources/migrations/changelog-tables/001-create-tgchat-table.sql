--liquibase formatted sql

--changeset alex_pletnev:1
create table tgchat
(
    tg_chat_id bigint unique not null,
    tag        text unique              not null,
    created_at timestamp with time zone not null,
    primary key (tg_chat_id)
)
--rollback drop table tgchat
