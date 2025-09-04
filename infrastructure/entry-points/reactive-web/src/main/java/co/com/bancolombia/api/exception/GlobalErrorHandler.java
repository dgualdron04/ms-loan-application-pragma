package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.exception.model.ErrorResponse;
import exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalErrorHandler implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> next) {
        return next.handle(serverRequest)
                .onErrorResume(DomainException.class, ex -> {
                    return ServerResponse.status(ex.getStatus()).bodyValue(
                            ErrorResponse.builder()
                                    .errorCode(ex.getErrorCode())
                                    .title(ex.getTitle())
                                    .message(ex.getMessage())
                                    .errors(ex.getErrors())
                                    .status(ex.getStatus())
                                    .timestamp(ex.getTimestamp())
                                    .build()
                    );
                });
    }
}
