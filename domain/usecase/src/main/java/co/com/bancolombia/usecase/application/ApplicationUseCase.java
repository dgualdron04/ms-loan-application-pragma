package co.com.bancolombia.usecase.application;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationView;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.model.applicationstatus.ApplicationStatus;
import co.com.bancolombia.model.applicationstatus.gateways.ApplicationStatusRepository;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import exception.BusinessRuleViolatedException;
import gateways.CustomLogger;
import gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import utils.StatusType;

import java.util.UUID;

@RequiredArgsConstructor
public class ApplicationUseCase implements IApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusRepository applicationStatusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final TransactionalGateway transactionalGateway;

    public Mono<ApplicationView> save(ApplicationView application) {
        Mono<UUID> statusIdMono = applicationStatusRepository.findIdByName(application.getStatus());
        Mono<UUID> loanTypeIdMono = loanTypeRepository.findIdByName(application.getLoanType())
                .switchIfEmpty(
                        Mono.error(
                                new BusinessRuleViolatedException("El tipo de préstamo " + application.getLoanType() + " no existe")
                        )
                );
        return transactionalGateway.executeTransactional(
                Mono.zip(statusIdMono, loanTypeIdMono)
                .map(tuple -> Application.builder()
                            .idNumber(application.getIdNumber())
                            .email(application.getEmail())
                            .amount(application.getAmount())
                            .duration(application.getDuration())
                            .statusId(tuple.getT1())
                            .loanTypeId(tuple.getT2())
                            .build()
                )
                .flatMap(applicationRepository::save)
                .map(saved -> ApplicationView.builder()
                        .idNumber(saved.getIdNumber())
                        .email(saved.getEmail())
                        .amount(saved.getAmount())
                        .duration(saved.getDuration())
                        .status(application.getStatus())
                        .loanType(application.getLoanType())
                        .build()
                )
        );
    }
}
