package edu.java.scrapper;

import edu.java.model.TgChat;
import edu.java.repositories.jdbc.JdbcTgChatRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcTgChatTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatRepository tgchatRepository;

    private TgChat tgchat;

    @BeforeEach
    void setUp() {
        tgchat = new TgChat();
        tgchat.setTgChatId(System.currentTimeMillis());
        tgchat.setTag("TestTag");
        tgchat.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        TgChat tgchat = new TgChat();
        tgchat.setTgChatId(1L);
        tgchat.setTag("TestTag");
        tgchat.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Optional<TgChat> savedTgchat = tgchatRepository.save(tgchat);

        assertTrue(savedTgchat.isPresent(), "Saved tgchat should be present");
        assertEquals(tgchat.getTgChatId(), savedTgchat.get().getTgChatId(), "TGChat IDs should match");
        assertEquals(tgchat.getTag(), savedTgchat.get().getTag(), "Tags should match");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        // Предварительно добавим TgChat в базу
        TgChat tgchat = new TgChat();
        tgchat.setTgChatId(2L); // Пример ID. В реальном случае лучше использовать автогенерируемый ID.
        tgchat.setTag("TestTagToRemove");
        tgchat.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        tgchatRepository.save(tgchat);

        // Удаление
        tgchatRepository.deleteByTgchatId(tgchat.getTgChatId());

        // Проверка удаления
        Optional<TgChat> foundTgchat = tgchatRepository.findByTgchatId(tgchat.getTgChatId());
        assertFalse(foundTgchat.isPresent(), "TgChat should not be present after deletion");
    }

    @Test
    @Transactional
    @Rollback
    void saveAndFindByTgchatIdTest() {
        tgchatRepository.save(tgchat);
        Optional<TgChat> foundTgchat = tgchatRepository.findByTgchatId(tgchat.getTgChatId());

        assertTrue(foundTgchat.isPresent(), "TgChat should be found by ID after saving");
        assertEquals(tgchat.getTgChatId(), foundTgchat.get().getTgChatId(), "TgChat IDs should match");
        assertEquals(tgchat.getTag(), foundTgchat.get().getTag(), "TgChat tags should match");
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        // Сначала сохраняем две tgChats для проверки
        tgchatRepository.save(tgchat);

        TgChat anotherTgChat = new TgChat();
        anotherTgChat.setTgChatId(System.currentTimeMillis() + 1000); // Гарантируем уникальность ID
        anotherTgChat.setTag("AnotherTestTag");
        anotherTgChat.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        tgchatRepository.save(anotherTgChat);

        List<TgChat> tgChats = tgchatRepository.findAll();

        assertTrue(tgChats.size() >= 2, "Should find at least two tgChats");
        assertTrue(
            tgChats.stream().anyMatch(t -> t.getTgChatId() == tgchat.getTgChatId()),
            "Should find the first saved tgchat"
        );
        assertTrue(
            tgChats.stream().anyMatch(t -> t.getTgChatId() == anotherTgChat.getTgChatId()),
            "Should find the second saved tgchat"
        );
    }

    @Test
    @Transactional
    @Rollback
    void deleteByTgchatIdTest() {
        tgchatRepository.save(tgchat);
        tgchatRepository.deleteByTgchatId(tgchat.getTgChatId());

        Optional<TgChat> foundTgchat = tgchatRepository.findByTgchatId(tgchat.getTgChatId());
        assertFalse(foundTgchat.isPresent(), "TgChat should not be present after deletion");
    }
}

