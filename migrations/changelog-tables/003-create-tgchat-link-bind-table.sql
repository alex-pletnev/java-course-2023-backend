--liquibase formatted sql

--changeset alex_pletnev:3
create table tgchat_link_bind
(
    tgchat_id bigint references tgchat (tgchat_id),
    link_id   bigint references link (link_id),
    primary key (tgchat_id, link_id)
)
--rollback drop table tgchat_link_bind
