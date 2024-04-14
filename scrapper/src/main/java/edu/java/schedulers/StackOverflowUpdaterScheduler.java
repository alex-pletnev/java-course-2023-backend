package edu.java.schedulers;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.LinkUpdate;
import edu.java.model.Link;
import edu.java.model.TgChatLinkBind;
import edu.java.services.BotServiceClient;
import edu.java.services.LinkService;
import edu.java.services.StackOverflowService;
import edu.java.services.TgChatLinkBindService;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
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
public class StackOverflowUpdaterScheduler implements LinkUpdaterScheduler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String STACK_OVERFLOW_BASE_URL = "https://stackoverflow.com";
    private static final int QUESTION_ID_IN_URL_INDEX = 2;

    private final StackOverflowService stackOverflowService;
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
            .filter(link -> link.getUrl().startsWith(STACK_OVERFLOW_BASE_URL))
            .map(link -> {
                try {
                    URI linkUri = new URI(link.getUrl());
                    long questionId = Long.parseLong(linkUri.getPath()
                        .split("/")[QUESTION_ID_IN_URL_INDEX]);
                    var hasNewAnswers = !stackOverflowService
                        .getNewAnswersFromStackOverflow(
                            questionId,
                            link.getLastCheckAt().toInstant().atOffset(ZoneOffset.UTC)
                        )
                        .toStream().toList().isEmpty();
                    if (hasNewAnswers) {
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

        LOGGER.info("StackOverflowUpdaterScheduler.update() is completed");
    }
}
