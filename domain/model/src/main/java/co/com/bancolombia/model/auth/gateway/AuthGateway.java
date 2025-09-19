package co.com.bancolombia.model.auth.gateway;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.UserSearchFilters;
import co.com.bancolombia.model.user.UsersFiltered;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthGateway {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByIdNumber(Long idNumber);
    Flux<UsersFiltered> search(UserSearchFilters userSearchFilters);
}
