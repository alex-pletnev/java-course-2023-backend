--liquibase formatted sql

--changeset alex_pletnev:2
create table link
(
    link_id       bigint generated always as identity,
    url           text unique              not null,
    last_check_at timestamp with time zone not null,
    primary key (link_id)
)
--rollback drop table link
