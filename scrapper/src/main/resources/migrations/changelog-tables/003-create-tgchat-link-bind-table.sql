--liquibase formatted sql

--changeset alex_pletnev:3
create table tgchat_link_bind
(
    tg_chat_id bigint references tgchat (tg_chat_id) on delete cascade,
    link_id    bigint references link (link_id) on delete cascade,
    primary key (tg_chat_id, link_id)
)
--rollback drop table tgchat_link_bind
