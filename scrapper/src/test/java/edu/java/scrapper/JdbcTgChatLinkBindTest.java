package edu.java.scrapper;

import edu.java.model.Link;
import edu.java.model.TgChat;
import edu.java.model.TgChatLinkBind;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import edu.java.repositories.jdbc.JdbcTgChatLinkBindRepository;
import edu.java.repositories.jdbc.JdbcTgChatRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
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
class JdbcTgChatLinkBindTest extends IntegrationTest {

    @Autowired
    private JdbcTgChatLinkBindRepository tgchatLinkBindRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcTgChatRepository jdbcTgchatRepository;

    private TgChatLinkBind tgChatLinkBind;
    private Link link;

    @BeforeEach
    void setUp() {

        TgChat tgchat = new TgChat();
        tgchat.setTgChatId(1L);
        tgchat.setTag("ex1");
        tgchat.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        jdbcTgchatRepository.save(tgchat);

        link = new Link();
        link.setUrl("http://ex1");
        link.setLastCheckAt(new Timestamp(System.currentTimeMillis()));
        link = jdbcLinkRepository.save(link).orElseThrow();

        tgChatLinkBind = new TgChatLinkBind();
        tgChatLinkBind.setTgChatId(tgchat.getTgChatId());
        tgChatLinkBind.setLinkId(link.getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void saveAndFindByTgchatIdAndLinkIdTest() {
        tgchatLinkBindRepository.save(tgChatLinkBind);
        Optional<TgChatLinkBind> foundBind =
            tgchatLinkBindRepository.findByTgchatIdAndLinkId(tgChatLinkBind.getTgChatId(), tgChatLinkBind.getLinkId());

        assertTrue(foundBind.isPresent(), "TgChatLinkBind should be found after saving");
        System.err.println(tgChatLinkBind.getTgChatId() + "------" + foundBind.get());
        assertEquals(tgChatLinkBind.getTgChatId(), foundBind.get().getTgChatId(), "TgChat IDs should match");
        assertEquals(tgChatLinkBind.getLinkId(), foundBind.get().getLinkId(), "Link IDs should match");
    }

    @Test
    @Transactional
    @Rollback
    void deleteByTgchatIdAndLinkIdTest() {
        tgchatLinkBindRepository.save(tgChatLinkBind);
        tgchatLinkBindRepository.deleteByTgchatIdAndLinkId(tgChatLinkBind.getTgChatId(), tgChatLinkBind.getLinkId());

        Optional<TgChatLinkBind> foundBind =
            tgchatLinkBindRepository.findByTgchatIdAndLinkId(tgChatLinkBind.getTgChatId(), tgChatLinkBind.getLinkId());
        assertFalse(foundBind.isPresent(), "TgChatLinkBind should not be present after deletion");
    }

    @Test
    @Transactional
    @Rollback
    void deleteAllByTgchatIdTest() {
        Link link2 = new Link();
        link2.setUrl("http://ex2");
        link2.setLastCheckAt(new Timestamp(System.currentTimeMillis()));

        Link link3 = new Link();
        link3.setUrl("http://ex3");
        link3.setLastCheckAt(new Timestamp(System.currentTimeMillis()));
        link2 = jdbcLinkRepository.save(link2).orElseThrow();
        link3 = jdbcLinkRepository.save(link3).orElseThrow();

        TgChatLinkBind tgChatLinkBind2 = new TgChatLinkBind();
        tgChatLinkBind2.setTgChatId(1L);
        tgChatLinkBind2.setLinkId(link2.getLinkId());
        TgChatLinkBind tgChatLinkBind3 = new TgChatLinkBind();
        tgChatLinkBind3.setTgChatId(1L);
        tgChatLinkBind3.setLinkId(link3.getLinkId());

        tgchatLinkBindRepository.save(tgChatLinkBind2);
        tgchatLinkBindRepository.save(tgChatLinkBind3);

        tgchatLinkBindRepository.deleteAllByTgchatId(1L);
        List<TgChatLinkBind> foundBinds = tgchatLinkBindRepository.findAllByTgchatId(1L);

        assertTrue(foundBinds.isEmpty(), "No TgchatLinkBinds should be present after deleting all by tgchatId");
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        tgchatLinkBindRepository.save(tgChatLinkBind);
        List<TgChatLinkBind> allBinds = tgchatLinkBindRepository.findAll();

        assertFalse(allBinds.isEmpty(), "Should find at least one TgChatLinkBind");
    }

    @Test
    @Transactional
    @Rollback
    void findAllByTgchatIdTest() {
        tgchatLinkBindRepository.save(tgChatLinkBind);
        List<TgChatLinkBind> bindsByTgchatId = tgchatLinkBindRepository.findAllByTgchatId(tgChatLinkBind.getTgChatId());

        assertFalse(bindsByTgchatId.isEmpty(), "Should find at least one TgChatLinkBind by tgchatId");
    }

    @AfterEach
    void close() {
        jdbcTgchatRepository.deleteByTgchatId(1L);
        jdbcLinkRepository.deleteByLinkId(link.getLinkId());
    }
}

