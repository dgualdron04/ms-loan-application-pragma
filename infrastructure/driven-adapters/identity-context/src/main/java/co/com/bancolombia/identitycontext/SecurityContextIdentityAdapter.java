package co.com.bancolombia.identitycontext;

import co.com.bancolombia.model.identity.Identity;
import co.com.bancolombia.model.identity.gateways.IdentityRepository;
import exception.BusinessRuleViolatedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import utils.RoleTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SecurityContextIdentityAdapter implements IdentityRepository {

    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    @Override
    public Mono<Identity> current() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .ofType(JwtAuthenticationToken.class)
                .switchIfEmpty(Mono.error(new BusinessRuleViolatedException("No JWT authentication present")))
                .map(JwtAuthenticationToken::getToken)
                .flatMap(jwt -> {
                    String email = Optional.ofNullable(jwt.getClaimAsString(EMAIL_CLAIM))
                            .orElse(jwt.getSubject());

                    List<String> roles = jwt.getClaimAsStringList(ROLE_CLAIM);

                    String roleCode = (roles == null || roles.isEmpty()) ? null : roles.get(0);

                    if (roleCode == null || roleCode.isBlank()) {
                        return Mono.error(new BusinessRuleViolatedException("JWT without role"));
                    }

                    RoleTypes role = RoleTypes.fromCode(roleCode);
                    return Mono.just(new Identity(email, role));
                });
    }

}
