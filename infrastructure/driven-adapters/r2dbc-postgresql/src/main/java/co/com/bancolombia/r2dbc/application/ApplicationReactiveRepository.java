package co.com.bancolombia.r2dbc.application;

import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, UUID>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

}
