package edu.java.controllers;

import edu.java.dto.AddTgChatRequest;
import edu.java.services.TgChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TgChatController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final TgChatService tgChatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTgChat(@RequestBody @Valid AddTgChatRequest addTgChatRequest) {
        LOGGER.info("tg-chat id: {} registered", addTgChatRequest);
        tgChatService.registerTgChat(addTgChatRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTgChat(@PathVariable long id) {
        LOGGER.info("tg-chat id: {} deleted", id);
        tgChatService.deleteTgChat(id);
    }
}
