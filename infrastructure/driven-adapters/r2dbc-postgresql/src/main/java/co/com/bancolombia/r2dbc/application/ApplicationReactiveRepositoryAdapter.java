package co.com.bancolombia.r2dbc.application;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Application/* change for domain model */,
    ApplicationEntity/* change for adapter model */,
    UUID,
    ApplicationReactiveRepository
> implements ApplicationRepository {
    public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Application.class));
    }

    @Override
    public Mono<Application> save(Application application) {
        return super.save(application);
    }

}
