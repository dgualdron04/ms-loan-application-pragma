package co.com.bancolombia.r2dbc.application_status;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.model.applicationstatus.ApplicationStatus;
import co.com.bancolombia.model.applicationstatus.gateways.ApplicationStatusRepository;
import co.com.bancolombia.r2dbc.entity.ApplicationStatusEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import utils.StatusType;

import java.util.UUID;

@Repository
public class ApplicationStatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    ApplicationStatus/* change for domain model */,
    ApplicationStatusEntity/* change for adapter model */,
    UUID,
    ApplicationStatusReactiveRepository
> implements ApplicationStatusRepository {
    public ApplicationStatusReactiveRepositoryAdapter(ApplicationStatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, ApplicationStatus.class));
    }

    @Override
    public Mono<UUID> findIdByName(StatusType status) {
        return repository.findByName(status.getName())
                .map(ApplicationStatusEntity::getId);
    }

}
