package co.com.bancolombia.model.identity.gateways;

import co.com.bancolombia.model.identity.Identity;
import reactor.core.publisher.Mono;

public interface IdentityRepository {
    Mono<Identity> current();
}
