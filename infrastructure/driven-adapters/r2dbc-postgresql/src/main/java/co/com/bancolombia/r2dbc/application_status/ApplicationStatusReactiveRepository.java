package co.com.bancolombia.r2dbc.application_status;

import co.com.bancolombia.model.applicationstatus.ApplicationStatus;
import co.com.bancolombia.r2dbc.entity.ApplicationStatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationStatusReactiveRepository extends ReactiveCrudRepository<ApplicationStatusEntity, UUID>, ReactiveQueryByExampleExecutor<ApplicationStatusEntity> {
    Mono<ApplicationStatusEntity> findByName(String name);
}
