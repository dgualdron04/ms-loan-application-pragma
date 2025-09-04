package co.com.bancolombia.usecase.application;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IApplicationUseCase {
    Mono<ApplicationView> save(ApplicationView application);
}
