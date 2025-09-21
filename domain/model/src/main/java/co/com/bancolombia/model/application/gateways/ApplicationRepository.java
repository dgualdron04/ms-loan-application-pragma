package co.com.bancolombia.model.application.gateways;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationList;
import co.com.bancolombia.model.application.ApplicationSearchFilters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;

import java.util.Collection;
import java.util.UUID;

public interface ApplicationRepository {
    Mono<Application> save(Application application);
    Mono<Boolean> existsByIdNumberAndStatusId(Long idNumber, UUID statusId);
    Flux<ApplicationList> search(ApplicationSearchFilters applicationSearchFilters);
    Flux<ApplicationList> getAllApplicationList();
    Mono<PageResult<ApplicationList>> searchPaged(ApplicationSearchFilters userSearchFilters, PageOptions pageOptions, Collection<String> allowedEmails);
    Mono<PageResult<ApplicationList>> getAllApplicationListPaged(PageOptions pageOptions);

}
