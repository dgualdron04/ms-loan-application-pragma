package co.com.bancolombia.model.auth.gateway;

import reactor.core.publisher.Mono;

public interface AuthGateway {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
}
