package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.CreateApplicationDTO;
import co.com.bancolombia.api.exception.model.InternalException;
import co.com.bancolombia.api.mapper.ApplicationApiMapper;
import co.com.bancolombia.usecase.application.IApplicationUseCase;
import exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final IApplicationUseCase applicationUseCase;
    private final ApplicationApiMapper applicationApiMapper;

    public Mono<ServerResponse> listenSaveApplication(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateApplicationDTO.class)
                .map(applicationApiMapper::toDomain)
                .flatMap(applicationUseCase::save)
                .map(applicationApiMapper::toResponse)
                .flatMap(res -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res))
                .onErrorResume(ex -> Mono.error(ex instanceof DomainException ? ex : new InternalException(ex, null)));
    }
}
