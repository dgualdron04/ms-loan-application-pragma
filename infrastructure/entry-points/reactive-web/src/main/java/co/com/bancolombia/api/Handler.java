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
import utils.pagination.PageOptions;
import utils.pagination.PageResult;
import utils.pagination.SortOrder;

import java.math.BigDecimal;
import java.util.List;

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
        ApplicationSearchFilters filters = searchFilters(serverRequest);

        boolean existPaging = serverRequest.queryParam("page").isPresent()
                || serverRequest.queryParam("size").isPresent()
                || serverRequest.queryParam("sort").isPresent();

        if (!existPaging) {
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(applicationUseCase.findApplications(filters), ApplicationList.class);
        }

        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(20);

        if (page <= 0) page = 0;
        if (size <= 0) size = 20;
        if (size > 100) size = 100;

        List<SortOrder> sort = serverRequest.queryParams().getOrDefault("sort", List.of())
                .stream()
                .map(s -> {
                    String[] p = s.split(",", 2);
                    String field = p[0].trim();
                    boolean asc = p.length < 2 || !"desc".equalsIgnoreCase(p[1].trim());
                    return new SortOrder(field, asc);
                })
                .toList();

        PageOptions pageOptions = new PageOptions(page, size, sort);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(applicationUseCase.findApplicationsPaged(filters, pageOptions), PageResult.class);

    }

    private ApplicationSearchFilters searchFilters(ServerRequest serverRequest) {
        return new ApplicationSearchFilters(
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
    }
}
