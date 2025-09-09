package co.com.bancolombia.model.application.gateways;

import co.com.bancolombia.model.application.Application;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationRepository {
    Mono<Application> save(Application application);
    Mono<Boolean> existsByIdNumberAndStatusId(Long idNumber, UUID statusId);
}
