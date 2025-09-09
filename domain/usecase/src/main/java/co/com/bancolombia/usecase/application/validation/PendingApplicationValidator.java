package co.com.bancolombia.usecase.application.validation;

import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import exception.BusinessRuleViolatedException;
import reactor.core.publisher.Mono;
import utils.StatusType;

import java.util.UUID;

public class PendingApplicationValidator {
    private PendingApplicationValidator() {}

    public static Mono<Void> validate(String statusName, Long idNumber, Mono<UUID> statusIdMono, ApplicationRepository applicationRepository) {
        if (!StatusType.PENDING_REVIEW.getName().equalsIgnoreCase(statusName)) {
            return Mono.empty();
        }

        return statusIdMono.flatMap(stId -> applicationRepository.existsByIdNumberAndStatusId(idNumber, stId))
                .flatMap(exists -> exists ? Mono.error(new BusinessRuleViolatedException("There is already a 'Pending review' request for this user.")) : Mono.empty());

    }
}
