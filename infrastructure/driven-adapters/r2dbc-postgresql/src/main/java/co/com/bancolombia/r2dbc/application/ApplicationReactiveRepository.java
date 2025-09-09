package co.com.bancolombia.r2dbc.application;

import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, UUID>, ReactiveQueryByExampleExecutor<ApplicationEntity> {
    Mono<Boolean> existsByIdNumberAndStatusId(Long idNumber, UUID statusId);
}
