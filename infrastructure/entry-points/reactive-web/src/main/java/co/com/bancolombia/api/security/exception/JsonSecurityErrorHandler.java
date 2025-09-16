package co.com.bancolombia.api.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import utils.ErrorTypes;

import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JsonSecurityErrorHandler implements ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {

    private  final ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return write(exchange, new SecurityAppException(ErrorTypes.ACCESS_DENIED));
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {
        return write(exchange, new SecurityAppException(ErrorTypes.ACCESS_DENIED));
    }

    private SecurityAppException map401(AuthenticationException ex) {
        if (ex instanceof OAuth2AuthenticationException oae) {
            OAuth2Error err = oae.getError();
            String code = err.getErrorCode();
            String desc = err.getDescription() == null ? "" : err.getDescription();

            if ("invalid_request".equals(code)) {
                return new SecurityAppException(ErrorTypes.TOKEN_MISSING);
            }

            if ("insufficient_scope".equals(code)) {
                return new SecurityAppException(ErrorTypes.ACCESS_DENIED);
            }

            if ("invalid_token".equals(code)) {
                if (desc.contains("expired") || desc.contains("expir")) {
                    return new SecurityAppException(ErrorTypes.TOKEN_EXPIRED);
                }

                if (desc.contains("signature") || desc.contains("sig")) {
                    return new SecurityAppException(ErrorTypes.TOKEN_SIGNATURE_INVALID);
                }
                return new SecurityAppException(ErrorTypes.TOKEN_INVALID);
            }
            return new SecurityAppException(ErrorTypes.AUTHENTICATION_FAILED);
        }

        if (ex.getCause() instanceof JwtValidationException jve) {
            String msg = jve.getMessage().toLowerCase();
            if (msg.contains("expired")) {
                return new SecurityAppException(ErrorTypes.TOKEN_EXPIRED);
            }
            if (msg.contains("signature")) {
                return new SecurityAppException(ErrorTypes.TOKEN_SIGNATURE_INVALID);
            }
            return new SecurityAppException(ErrorTypes.TOKEN_INVALID);
        }

        return new SecurityAppException(ErrorTypes.AUTHENTICATION_FAILED);
    }

    private Mono<Void> write(ServerWebExchange exchange, SecurityAppException err) {
        ErrorTypes type = err.getErrorType();
        ServerHttpResponse resp = exchange.getResponse();

        resp.setStatusCode(HttpStatus.valueOf(type.getStatus()));
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "errorCode", type.getErrorCode(),
                "title", type.getTitle(),
                "message", type.getMessage(),
                "status", type.getStatus(),
                "timestamp", Instant.now().toString(),
                "path", exchange.getRequest().getPath().value(),
                "errors", Optional.ofNullable(err.getDetailsOverride()).orElse(type.getErrors())
        );

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(body))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(bytes -> resp.writeWith(Mono.just(resp.bufferFactory().wrap(bytes))));

    }

}
