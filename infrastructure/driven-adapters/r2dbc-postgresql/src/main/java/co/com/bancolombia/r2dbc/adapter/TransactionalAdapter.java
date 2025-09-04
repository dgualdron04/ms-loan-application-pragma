package co.com.bancolombia.r2dbc.adapter;

import gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionalAdapter implements TransactionalGateway {
    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> executeTransactional(Mono<T> action) {
        return transactionalOperator.transactional(action);
    }

    @Override
    public <T> Flux<T> executeTransactional(Flux<T> action) {
        return transactionalOperator.transactional(action);
    }
}
