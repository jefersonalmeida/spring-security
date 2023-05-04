package com.gestorsistemas.client;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class ClientController {

    WebClient webClient;

    public ClientController(final WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://127.0.0.1:9090").build();
    }

    @GetMapping("home")
    public Mono<String> home(
            @RegisteredOAuth2AuthorizedClient final OAuth2AuthorizedClient client,
            @AuthenticationPrincipal final OidcUser oidcUser
    ) {
        return Mono.just("""
                        <h2>Access Token: %s</h2>
                        <h2>Refresh Token: %s</h2>
                        <h2>ID Token: %s</h2>
                        <h2>Claims: %s</h2>
                        """.formatted(
                        Optional.ofNullable(client.getAccessToken()).map(AbstractOAuth2Token::getTokenValue).orElse(null),
                        Optional.ofNullable(client.getRefreshToken()).map(AbstractOAuth2Token::getTokenValue).orElse(null),
                        Optional.ofNullable(oidcUser.getIdToken()).map(AbstractOAuth2Token::getTokenValue).orElse(null),
                        oidcUser.getClaims()
                )
        );
    }

    @GetMapping("tasks")
    public Mono<String> getTasks(@RegisteredOAuth2AuthorizedClient final OAuth2AuthorizedClient client) {
        return this.webClient.get()
                .uri("tasks")
                .header("Authorization", "Bearer %s".formatted(client.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToMono(String.class);
    }
}
