package co.com.bancolombia.r2dbc.application.view;

import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import co.com.bancolombia.r2dbc.entity.ApplicationsWithStatusAndLoanTypesView;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationViewReactiveRepository extends ReactiveCrudRepository<ApplicationsWithStatusAndLoanTypesView, UUID> {
    Flux<ApplicationsWithStatusAndLoanTypesView> findAll();
}
