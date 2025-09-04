package gateways;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionalGateway {
    <T> Mono<T> executeTransactional(Mono<T> action);
    <T> Flux<T> executeTransactional(Flux<T> action);
}
