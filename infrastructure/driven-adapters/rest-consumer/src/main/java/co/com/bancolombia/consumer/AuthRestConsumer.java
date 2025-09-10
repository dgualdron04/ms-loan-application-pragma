package co.com.bancolombia.consumer;

import co.com.bancolombia.model.auth.gateway.AuthGateway;
import gateways.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthRestConsumer implements AuthGateway {
    private final WebClient client;
    private final CustomLogger logger;

    public Mono<Boolean> existsByEmail(String email) {
        return client.get()
                .uri(uri -> uri.path("/api/v1/usuarios/email/{email}").build(email))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExistsResponse.class)
                .map(ExistsResponse::isExists);
    }

    public Mono<Boolean> existsByIdNumber(Long idNumber) {
        return client.get()
                .uri(uri -> uri.path("/api/v1/usuarios/idNumber/{idNumber}").build(idNumber))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExistsResponse.class)
                .map(ExistsResponse::isExists);
    }
}
