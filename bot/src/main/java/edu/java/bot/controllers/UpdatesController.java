package edu.java.bot.controllers;

import edu.java.bot.dto.LinkUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private static final int OK = 200;
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping
    public HttpStatusCode postNewUpdates(@RequestBody LinkUpdate linkUpdate) {
        LOGGER.info("new updates: {}", linkUpdate);
        return HttpStatusCode.valueOf(OK);
    }

}
