package edu.java.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registerTgChat(@PathVariable long id) {
        LOGGER.info("tg-chat id: {} registered", id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTgChat(@PathVariable long id) {
        LOGGER.info("tg-chat id: {} deleted", id);
    }
}
