package edu.java.services.jdbc;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.exceptions.UndefinedUrlException;
import edu.java.exceptions.UrlFormatException;
import edu.java.model.Link;
import edu.java.model.TgChatLinkBind;
import edu.java.repositories.jdbc.JdbcLinkRepository;
import edu.java.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcTgChatService jdbcTgChatService;
    private final JdbcTgChatLinkBindService jdbcTgChatLinkBindService;

    @Override
    public ListLinksResponse getAllLinksByTgChatId(long tgChatId) {
        jdbcTgChatService.getByTgChatId(tgChatId);

        var links = jdbcLinkRepository
            .findAllByLinkIdList(
                jdbcTgChatLinkBindService
                    .getAllByTgchatId(tgChatId)
                    .stream()
                    .map(TgChatLinkBind::getLinkId)
                    .toList())
            .stream()
            .map(this::createLinkResponse)
            .toList();
        return new ListLinksResponse(links, links.size());

    }

    @Transactional
    @Override
    public LinkResponse addLinkForTgChatId(long tgChatId, AddLinkRequest addLinkRequest) {
        jdbcTgChatService.getByTgChatId(tgChatId);

        var newLink = jdbcLinkRepository
            .findByUrl(addLinkRequest.link().toString())
            .map(this::createLinkResponse)
            .orElseGet(
                () -> {
                    Link link = new Link();
                    link.setUrl(addLinkRequest.link().toString());
                    link.setLastCheckAt(new Timestamp(System.currentTimeMillis()));
                    return jdbcLinkRepository
                        .save(link)
                        .map(this::createLinkResponse)
                        .orElseThrow();
                }
            );

        jdbcTgChatLinkBindService.addTgChatLinkBind(tgChatId, newLink.id());

        return newLink;
    }

    @Transactional
    @Override
    public void deleteLinkForTgChatId(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        jdbcTgChatService.getByTgChatId(tgChatId);

        var targetLink = jdbcLinkRepository
            .findByUrl(removeLinkRequest.link().toString())
            .orElseThrow(UndefinedUrlException::new);
        jdbcTgChatLinkBindService.deleteByTgchatIdAndLinkId(tgChatId, targetLink.getLinkId());
        if (Boolean.FALSE.equals(jdbcTgChatLinkBindService.existByLinkId(targetLink.getLinkId()))) {
            jdbcLinkRepository.deleteByLinkId(targetLink.getLinkId());
        }
    }

    @Override
    public List<Link> getAllWhereLastCheckAtAfterParam(Timestamp timestamp) {
        return jdbcLinkRepository.findAllWhereLastCheckAtAfterParam(timestamp);
    }

    private LinkResponse createLinkResponse(Link link) {
        try {
            return new LinkResponse(link.getLinkId(), new URI(link.getUrl()));
        } catch (URISyntaxException e) {
            throw new UrlFormatException(e);
        }
    }

    @Override
    public void updateAllLastCheckAtByLinkIdList(List<Long> linkIdList, Timestamp newLastCheckValue) {
        jdbcLinkRepository.updateAllLastCheckAtByLinkIdList(linkIdList, newLastCheckValue);
    }
}
