package co.com.bancolombia.consumer;

import co.com.bancolombia.model.user.UserSearchFilters;
import co.com.bancolombia.consumer.response.ExistsResponse;
import co.com.bancolombia.model.user.UsersFiltered;
import co.com.bancolombia.model.auth.gateway.AuthGateway;
import gateways.CustomLogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Service
public class AuthRestConsumer implements AuthGateway {

    private final WebClient client;
    private final CustomLogger logger;

    public AuthRestConsumer(@Qualifier("authWebClient") WebClient client, CustomLogger logger) {
        this.client = client;
        this.logger = logger;
    }

    public Mono<Boolean> existsByEmail(String email) {
        return client.get()
                .uri(uri -> uri.path("/api/v1/usuarios/email/{email}").build(email))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExistsResponse.class)
                .map(ExistsResponse::isExists);
    }

    public Mono<Boolean> existsByIdNumber(Long idNumber) {
        return client.get()
                .uri(uri -> uri.path("/api/v1/usuarios/idNumber/{idNumber}").build(idNumber))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExistsResponse.class)
                .map(ExistsResponse::isExists);
    }

    public Flux<UsersFiltered> search(UserSearchFilters userSearchFilters) {
        URI uriWithFilters = buildSearchUri(userSearchFilters);
        return client.get()
                .uri(uriWithFilters.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UsersFiltered.class);
    }

    private static URI buildSearchUri(UserSearchFilters userSearchFilters) {
            UriComponentsBuilder uriUsers = UriComponentsBuilder.fromPath("/api/v1/usuarios");
            uriUsers.queryParamIfPresent("firstName", compareOptionalText(userSearchFilters.firstName()));
            uriUsers.queryParamIfPresent("lastName", compareOptionalText(userSearchFilters.lastName()));
            uriUsers.queryParamIfPresent("email", compareOptionalText(userSearchFilters.email()));
            uriUsers.queryParamIfPresent("birthDateFrom", Optional.ofNullable(userSearchFilters.birthDateFrom()));
            uriUsers.queryParamIfPresent("birthDateTo", Optional.ofNullable(userSearchFilters.birthDateTo()));
            uriUsers.queryParamIfPresent("idNumber", compareOptionalText(userSearchFilters.idNumber()));
            uriUsers.queryParamIfPresent("phone", compareOptionalText(userSearchFilters.phone()));
            uriUsers.queryParamIfPresent("roleName", compareOptionalText(userSearchFilters.roleName()));
            uriUsers.queryParamIfPresent("minBaseSalary", Optional.ofNullable(userSearchFilters.minBaseSalary()));
            uriUsers.queryParamIfPresent("maxBaseSalary", Optional.ofNullable(userSearchFilters.maxBaseSalary()));
            return uriUsers.build().toUri();
    }

    private static Optional<String> compareOptionalText(String txt) {
        return (txt == null || txt.isBlank()) ? Optional.empty() : Optional.of(txt);
    }
}
