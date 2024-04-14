package edu.java.controllers;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.services.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LinksController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final LinkService linkService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListLinksResponse getAllLinksByTgChatId(@RequestHeader("Tg-Chat-Id") long tgChatId) {
        LOGGER.info("get all links by tg-chat id: {} ", tgChatId);
        return linkService.getAllLinksByTgChatId(tgChatId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LinkResponse addLinkForTgChatId(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        LOGGER.info("add link: {} for tg-chat id: {} ", addLinkRequest.link(), tgChatId);
        return linkService.addLinkForTgChatId(tgChatId, addLinkRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLinkForTgChatId(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        LOGGER.info("delete link: {} for tg-chat id: {} ", removeLinkRequest.link(), tgChatId);
        linkService.deleteLinkForTgChatId(tgChatId, removeLinkRequest);
    }

}
