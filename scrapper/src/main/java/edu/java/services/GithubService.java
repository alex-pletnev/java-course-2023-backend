package edu.java.services;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.github.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final WebClient githubClient;
    private final ApplicationConfig applicationConfig;

    public Mono<Repository> getNewAnswersFromGithub(String login, String repository) {
        return githubClient
            .get()
            .uri("/repos/{user}/{repository}", login, repository)
            .header("User-Agent", "request")
            .header("Authorization", applicationConfig.githubApiToken())
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.bodyToMono(RuntimeException.class)
                .flatMap(error -> Mono.error(new RuntimeException(
                    "Github RuntimeException with code " + clientResponse.statusCode()))))
            .bodyToMono(Repository.class);
    }
}
