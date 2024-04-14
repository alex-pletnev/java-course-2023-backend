package edu.java.scrapper;

import edu.java.model.Link;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import java.sql.Timestamp;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcLinkTest extends IntegrationTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link newLink = new Link();
        newLink.setUrl("http://example.com");
        newLink.setLastCheckAt(new Timestamp(System.currentTimeMillis()));

        Optional<Link> savedLink = linkRepository.save(newLink);

        assertTrue(savedLink.isPresent(), "Saved link should be present");
        assertEquals(newLink.getUrl(), savedLink.get().getUrl(), "URLs should match");
        assertNotNull(savedLink.get().getLinkId(), "Saved link should have an ID");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        // Предварительно добавим ссылку в базу
        Link link = new Link();
        link.setUrl("http://testremove.com");
        link.setLastCheckAt(new Timestamp(System.currentTimeMillis()));
        Link savedLink = linkRepository.save(link).orElseThrow();

        // Удаление
        linkRepository.deleteByLinkId(savedLink.getLinkId());

        // Проверка удаления
        Optional<Link> foundLink = linkRepository.findByLinkId(savedLink.getLinkId());
        assertFalse(foundLink.isPresent(), "Link should not be present after deletion");
    }
}
