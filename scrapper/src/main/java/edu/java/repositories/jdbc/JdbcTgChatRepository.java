package edu.java.repositories.jdbc;

import edu.java.model.TgChat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository {

    private final JdbcClient jdbcClient;

    @Transactional
    public Optional<TgChat> save(TgChat tgChat) {
        jdbcClient
            .sql("insert into tgchat (tg_chat_id, tag, created_at) VALUES (?, ?, ?)")
            .param(tgChat.getTgChatId())
            .param(tgChat.getTag())
            .param(tgChat.getCreatedAt())
            .update();
        return findByTgchatId(tgChat.getTgChatId());
    }

    public Optional<TgChat> findByTgchatId(long tgChatId) {
        return jdbcClient
            .sql("select * from tgchat where tg_chat_id = ?")
            .param(tgChatId)
            .query(TgChat.class)
            .optional();
    }

    @Transactional
    public void deleteByTgchatId(long tgChatId) {
        jdbcClient
            .sql("delete from tgchat where tg_chat_id = ?")
            .param(tgChatId)
            .update();
    }

    public List<TgChat> findAll() {
        return jdbcClient
            .sql("select * from tgchat")
            .query(TgChat.class)
            .list();
    }
}
