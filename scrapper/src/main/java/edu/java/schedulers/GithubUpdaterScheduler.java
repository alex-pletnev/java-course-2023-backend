package edu.java.schedulers;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.model.Link;
import edu.java.model.TgChatLinkBind;
import edu.java.services.BotServiceClient;
import edu.java.services.GithubService;
import edu.java.services.LinkService;
import edu.java.services.TgChatLinkBindService;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
@RequiredArgsConstructor
public class GithubUpdaterScheduler implements LinkUpdaterScheduler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String GITHUB_BASE_URL = "https://github.com";
    private static final int OWNER_URL_INDEX = 1;
    private static final int REPO_URL_INDEX = 2;

    private final GithubService githubService;
    private final LinkService linkService;
    private final TgChatLinkBindService tgChatLinkBindService;
    private final BotServiceClient botServiceClient;
    private final ApplicationConfig applicationConfig;

    @Override
    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        Timestamp checkTime =
            new Timestamp(System.currentTimeMillis() - applicationConfig.scheduler().forceCheckDelay().toMillis());
        List<Link> linkListForUpdate = linkService.getAllWhereLastCheckAtAfterParam(checkTime);

        var linkIdListForUpdate = linkListForUpdate.parallelStream()
            .filter(link -> link.getUrl().startsWith(GITHUB_BASE_URL))
            .map(link -> {
                try {
                    URI linkUri = new URI(link.getUrl());
                    var splitLinkUrl = linkUri.getPath()
                        .split("/");
                    var owner = splitLinkUrl[OWNER_URL_INDEX];
                    var repo = splitLinkUrl[REPO_URL_INDEX];

                    var hasUpdates = githubService
                        .getNewAnswersFromGithub(owner, repo)
                        .map(repository -> repository.getPushedAt().toInstant()
                            .isAfter(link.getLastCheckAt().toInstant()))
                        .block();

                    if (Boolean.TRUE.equals(hasUpdates)) {
                        List<Long> ids = tgChatLinkBindService
                            .getAllByLinkId(link.getLinkId())
                            .stream()
                            .map(TgChatLinkBind::getTgChatId)
                            .toList();
                        LinkUpdate linkUpdate = new LinkUpdate(
                            link.getLinkId(),
                            linkUri,
                            "new content!",
                            ids
                        );
                        botServiceClient
                            .postLinkUpdate(linkUpdate)
                            .subscribe(httpStatusCode -> LOGGER.info("Bot answer is: {}", httpStatusCode));
                    }
                } catch (URISyntaxException | NumberFormatException e) {
                    LOGGER.error("Error processing link: {} {}", link.getUrl(), e);
                }
                return link;
            })
            .map(Link::getLinkId)
            .toList();
        linkService.updateAllLastCheckAtByLinkIdList(linkIdListForUpdate, new Timestamp(System.currentTimeMillis()));

        LOGGER.info("GithubUpdaterScheduler.update() is completed");
    }
}
