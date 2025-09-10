package co.com.bancolombia.api;

import co.com.bancolombia.api.config.LoanPath;
import co.com.bancolombia.api.docs.ApplicationDocs;
import co.com.bancolombia.api.exception.GlobalErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final LoanPath loanPath;
    private final Handler applicationHandler;
    private final ApplicationDocs applicationDocs;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(GlobalErrorHandler globalErrorHandler) {
        var routes = route()
                .POST(loanPath.getLoans(), req -> true, applicationHandler::listenSaveApplication, applicationDocs.save())
                .build();

        return routes.filter(globalErrorHandler);
    }
}
