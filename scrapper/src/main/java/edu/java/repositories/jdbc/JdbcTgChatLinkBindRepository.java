package edu.java.repositories.jdbc;

import edu.java.model.TgChatLinkBind;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatLinkBindRepository {

    private final JdbcClient jdbcClient;

    @Transactional
    public Optional<TgChatLinkBind> save(TgChatLinkBind tgchatLinkBind) {
        jdbcClient
            .sql("insert into tgchat_link_bind (tg_chat_id, link_id) VALUES (?, ?)")
            .param(tgchatLinkBind.getTgChatId())
            .param(tgchatLinkBind.getLinkId())
            .update();
        return findByTgchatIdAndLinkId(tgchatLinkBind.getTgChatId(), tgchatLinkBind.getLinkId());
    }

    public Optional<TgChatLinkBind> findByTgchatIdAndLinkId(long tgchatId, long linkId) {
        return jdbcClient
            .sql("select * from tgchat_link_bind where tg_chat_id = ? and link_id = ?")
            .param(tgchatId)
            .param(linkId)
            .query(TgChatLinkBind.class)
            .optional();
    }

    @Transactional
    public void deleteByTgchatIdAndLinkId(long tgchatId, long linkId) {
        jdbcClient
            .sql("delete from tgchat_link_bind where tg_chat_id = ? and link_id = ?")
            .param(tgchatId)
            .param(linkId)
            .update();
    }

    @Transactional
    public void deleteAllByTgchatId(long tgChatId) {
        jdbcClient
            .sql("delete from tgchat_link_bind where tg_chat_id = ?")
            .param(tgChatId)
            .update();
    }

    public List<TgChatLinkBind> findAll() {
        return jdbcClient
            .sql("select * from tgchat_link_bind")
            .query(TgChatLinkBind.class)
            .list();
    }

    public List<TgChatLinkBind> findAllByTgchatId(long tgChatId) {
        return jdbcClient
            .sql("select * from tgchat_link_bind where tg_chat_id = ?")
            .param(tgChatId)
            .query(TgChatLinkBind.class)
            .list();
    }

    public List<TgChatLinkBind> findAllByLinkId(long linkId) {
        return jdbcClient
            .sql("select * from tgchat_link_bind where link_id = ?")
            .param(linkId)
            .query(TgChatLinkBind.class)
            .list();
    }

    public Boolean existByLinkId(long linkId) {
        return jdbcClient
            .sql("select 1 from tgchat_link_bind where link_id = ? limit 1")
            .param(linkId)
            .query(TgChatLinkBind.class)
            .optional()
            .isPresent();
    }
}
