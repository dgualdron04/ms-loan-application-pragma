package co.com.bancolombia.model.auth.gateway;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface AuthGateway {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
}
