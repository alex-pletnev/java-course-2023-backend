package edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient scrapperClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:8080");
        return WebClient.builder().uriBuilderFactory(factory).build();
    }
}
