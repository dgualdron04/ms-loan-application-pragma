package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.CreateApplicationDTO;
import co.com.bancolombia.api.exception.model.InternalException;
import co.com.bancolombia.api.mapper.ApplicationApiMapper;
import co.com.bancolombia.model.application.ApplicationList;
import co.com.bancolombia.model.application.ApplicationSearchFilters;
import co.com.bancolombia.usecase.application.IApplicationUseCase;
import exception.BusinessRuleViolatedException;
import exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Handler {
    private final IApplicationUseCase applicationUseCase;
    private final ApplicationApiMapper applicationApiMapper;

    @PreAuthorize("hasAuthority('CLIENT')")
    public Mono<ServerResponse> listenSaveApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateApplicationDTO.class)
                .map(applicationApiMapper::toDomain)
                .flatMap(applicationUseCase::save)
                .switchIfEmpty(Mono.error(new BusinessRuleViolatedException("APPLICATION_NOT_SAVED")))
                .map(applicationApiMapper::toResponse)
                .flatMap(res -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res))
                .onErrorResume(ex -> Mono.error(ex instanceof DomainException ? ex : new InternalException(ex, null)));
    }

    @PreAuthorize("hasAuthority('ADVISOR')")
    public Mono<ServerResponse> listenGetAllApplication(ServerRequest serverRequest) {
        ApplicationSearchFilters filters = new ApplicationSearchFilters(
                serverRequest.queryParam("firstName").filter(s -> !s.isBlank()).orElse(null),
                serverRequest.queryParam("lastName").filter(s -> !s.isBlank()).orElse(null),
                serverRequest.queryParam("email").filter(s -> !s.isBlank()).orElse(null),
                serverRequest.queryParam("amount").filter(s -> !s.isBlank()).map(BigDecimal::new).orElse(null),
                serverRequest.queryParam("duration").filter(s -> !s.isBlank()).map(Integer::valueOf).orElse(null),
                serverRequest.queryParam("loanType").filter(s -> !s.isBlank()).orElse(null),
                serverRequest.queryParam("interestRate").filter(s -> !s.isBlank()).map(Double::valueOf).orElse(null),
                serverRequest.queryParam("status").filter(s -> !s.isBlank()).orElse(null),
                serverRequest.queryParam("minBaseSalary").filter(s -> !s.isBlank()).map(Integer::valueOf).orElse(null),
                serverRequest.queryParam("maxBaseSalary").filter(s -> !s.isBlank()).map(Integer::valueOf).orElse(null)
        );

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(applicationUseCase.findApplications(filters), ApplicationList.class);
    }
}
