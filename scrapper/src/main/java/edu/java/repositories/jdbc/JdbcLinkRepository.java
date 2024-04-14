package edu.java.repositories.jdbc;

import edu.java.exceptions.DataBaseSaveException;
import edu.java.model.Link;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {

    private final JdbcClient jdbcClient;

    @Transactional
    public Optional<Link> save(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
            .sql("insert into link (url, last_check_at) VALUES (?, ?)")
            .param(link.getUrl())
            .param(link.getLastCheckAt())
            .update(keyHolder);
        try {
            return findByLinkId((long) keyHolder.getKeys().get("link_id"));
        } catch (NullPointerException ex) {
            throw new DataBaseSaveException(ex);
        }
    }

    public Optional<Link> findByLinkId(long linkId) {
        return jdbcClient
            .sql("select * from link where link_id = ?")
            .param(linkId)
            .query(Link.class)
            .optional();
    }

    public Optional<Link> findByUrl(String url) {
        return jdbcClient
            .sql("select * from link where url = ?")
            .param(url)
            .query(Link.class)
            .optional();
    }

    @Transactional
    public void deleteByLinkId(long linkId) {
        jdbcClient
            .sql("delete from link where link_id = ?")
            .param(linkId)
            .update();
    }

    public List<Link> findAllByLinkIdList(List<Long> linkIdList) {
        if (linkIdList.isEmpty()) {
            return List.of();
        }

        String inClause = String.join(",", Collections.nCopies(linkIdList.size(), "?"));
        String sql = "select * from link where link_id IN (" + inClause + ")";

        return jdbcClient
            .sql(sql)
            .params(linkIdList)
            .query(Link.class)
            .list();
    }

    public List<Link> findAll() {
        return jdbcClient
            .sql("select * from link")
            .query(Link.class)
            .list();
    }

    public List<Link> findAllWhereLastCheckAtAfterParam(Timestamp timestamp) {
        return jdbcClient
            .sql("select * from link where last_check_at < ?")
            .param(timestamp)
            .query(Link.class)
            .list();
    }

    public void updateAllLastCheckAtByLinkIdList(List<Long> linkIdList, Timestamp newLastCheckValue) {
        if (linkIdList.isEmpty()) {
            return;
        }

        String inClause = String.join(",", Collections.nCopies(linkIdList.size(), "?"));
        String sql = "update link set last_check_at = ? where link_id IN (" + inClause + ")";

        jdbcClient
            .sql(sql)
            .param(newLastCheckValue)
            .params(linkIdList)
            .update();
    }


}
