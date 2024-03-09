package edu.java.controllers;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {

    private static final Logger LOGGER = LogManager.getLogger();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getAllLinksByTgChatId(@RequestHeader("Tg-Chat-Id") long chatId) {
        LOGGER.info("get all links by tg-chat id: {} ", chatId);
        return new ListLinksResponse(new ArrayList<>(), 0);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkResponse addLinkForTgChatId(
        @RequestHeader("Tg-Chat-Id") long chatId, @RequestBody
    AddLinkRequest addLinkRequest
    ) throws URISyntaxException {
        LOGGER.info("add link: {} for tg-chat id: {} ", addLinkRequest.link(), chatId);
        return new LinkResponse(1, new URI("addExample"));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public LinkResponse deleteLinkForTgChatId(
        @RequestHeader("Tg-Chat-Id") long chatId, @RequestBody
    RemoveLinkRequest removeLinkRequest
    ) throws URISyntaxException {
        LOGGER.info("delete link: {} for tg-chat id: {} ", removeLinkRequest.link(), chatId);
        return new LinkResponse(1, new URI("deleteExample"));
    }

}
