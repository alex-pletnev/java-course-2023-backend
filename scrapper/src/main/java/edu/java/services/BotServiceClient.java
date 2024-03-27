package edu.java.services;

import edu.java.dto.ApiErrorResponse;
import edu.java.dto.LinkUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
public class BotServiceClient {

    private static final Logger LOGGER = LogManager.getLogger();
    private final WebClient botClient;

    @Autowired
    public BotServiceClient(WebClient botClient) {
        this.botClient = botClient;
    }

    public BotServiceClient(String baseUrl) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        this.botClient = WebClient.builder().uriBuilderFactory(factory).build();
    }

    public Mono<HttpStatusCode> postLinkUpdate(LinkUpdate linkUpdate) {
        return botClient
            .post()
            .uri("/updates")
            .bodyValue(linkUpdate)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(HttpStatusCode.class);
    }

}
