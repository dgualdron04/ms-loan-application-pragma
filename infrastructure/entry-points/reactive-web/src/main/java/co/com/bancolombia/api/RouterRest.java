package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanPath;
import co.com.bancolombia.api.exception.GlobalErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final LoanPath loanPath;
    private final Handler applicationHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(GlobalErrorHandler globalErrorHandler) {
        return route(POST(loanPath.getLoans()), applicationHandler::listenSaveApplication)
                .filter(globalErrorHandler);
    }
}
