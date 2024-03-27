--liquibase formatted sql

--changeset alex_pletnev:1
create table tgchat
(
    tgchat_id  bigint unique            not null,
    tag        text unique              not null,
    created_at timestamp with time zone not null,
    primary key (tgchat_id)
)
--rollback drop table tgchat
