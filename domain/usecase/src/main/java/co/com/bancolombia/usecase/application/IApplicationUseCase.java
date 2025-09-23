package co.com.bancolombia.usecase.application;

import co.com.bancolombia.model.application.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.StatusType;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;

import java.util.UUID;

public interface IApplicationUseCase {
    Mono<ApplicationView> save(ApplicationView application);
    Flux<ApplicationList> findApplications(ApplicationSearchFilters applicationSearchFilters);
    Mono<PageResult<ApplicationList>> findApplicationsPaged(ApplicationSearchFilters applicationSearchFilters, PageOptions pageOptions);
    Mono<UpdateStatusResult> updateStatusApplication(UpdateStatusApplication updateStatusApplication);
}
