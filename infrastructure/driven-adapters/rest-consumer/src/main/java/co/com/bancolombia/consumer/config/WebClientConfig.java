package co.com.bancolombia.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.*;


@Configuration
public class WebClientConfig {

    @Bean("authWebClient")
    public WebClient authWebClient(WebClient.Builder builder,
                                   @Value("${adapter.restconsumer.url}") String baseUrl) {

        ExchangeFilterFunction propagateBearer = (request, next) ->
                ReactiveSecurityContextHolder.getContext()
                        .map(ctx -> ctx.getAuthentication())
                        .map(auth -> {
                            String token = (auth instanceof JwtAuthenticationToken jat)
                                    ? jat.getToken().getTokenValue()
                                    : (auth != null && auth.getCredentials() != null ? auth.getCredentials().toString() : null);
                            if (token == null || token.isBlank()) return request;
                            return ClientRequest.from(request)
                                    .headers(h -> h.set(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                    .build();
                        })
                        .defaultIfEmpty(request)
                        .flatMap(next::exchange);

        ExchangeFilterFunction debug = (request, next) -> {
            String auth = request.headers().getFirst(HttpHeaders.AUTHORIZATION); // <-- getFirst
            System.out.println("[authWebClient] " + request.method() + " " + request.url());
            System.out.println("[authWebClient] Has Authorization: " + (auth != null && !auth.isBlank()));
            return next.exchange(request);
        };

        return builder
                .baseUrl(baseUrl)
                .filter(propagateBearer)
                .filter(debug)
                .build();
    }
}