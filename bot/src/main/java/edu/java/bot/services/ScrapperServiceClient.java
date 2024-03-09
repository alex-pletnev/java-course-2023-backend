package edu.java.bot.services;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
public class ScrapperServiceClient {

    private static final String TG_CHAT_URL = "/tg-chat/{id}";
    private static final String LINKS_URL = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    private static final Logger LOGGER = LogManager.getLogger();
    private final WebClient scrapperClient;

    @Autowired
    public ScrapperServiceClient(WebClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    public ScrapperServiceClient(String url) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        this.scrapperClient = WebClient.builder().uriBuilderFactory(factory).build();
    }

    public Mono<HttpStatusCode> registerNewChat(long tgChatId) {
        return scrapperClient
            .post()
            .uri(TG_CHAT_URL, tgChatId)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(HttpStatusCode.class);
    }

    public Mono<HttpStatusCode> deleteChat(long tgChatId) {
        return scrapperClient
            .delete()
            .uri(TG_CHAT_URL, tgChatId)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(HttpStatusCode.class);
    }

    public Mono<ListLinksResponse> getAllLinks(long tgChatId) {
        return scrapperClient
            .get()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<ListLinksResponse> removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return scrapperClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(removeLinkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<ListLinksResponse> addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        return scrapperClient
            .post()
            .uri(LINKS_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> {
                    LOGGER.info(apiErrorResponse);
                    return Mono.empty();
                }))
            .bodyToMono(ListLinksResponse.class);
    }

}
