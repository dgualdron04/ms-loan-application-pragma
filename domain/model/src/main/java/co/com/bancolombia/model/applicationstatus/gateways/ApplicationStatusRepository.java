package co.com.bancolombia.model.applicationstatus.gateways;

import co.com.bancolombia.model.applicationstatus.ApplicationStatus;
import reactor.core.publisher.Mono;
import utils.StatusType;

import java.util.UUID;

public interface ApplicationStatusRepository {
    Mono<UUID> findIdByName(StatusType status);
}