package co.com.bancolombia.model.user.gateways;

import reactor.core.publisher.Mono;

public interface UserClientGateway {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
}
