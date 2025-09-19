package co.com.bancolombia.usecase.application;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationList;
import co.com.bancolombia.model.application.ApplicationSearchFilters;
import co.com.bancolombia.model.application.ApplicationView;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.model.applicationstatus.gateways.ApplicationStatusRepository;
import co.com.bancolombia.model.auth.gateway.AuthGateway;
import co.com.bancolombia.model.identity.Identity;
import co.com.bancolombia.model.identity.gateways.IdentityRepository;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.model.user.UserSearchFilters;
import co.com.bancolombia.model.user.UsersFiltered;
import co.com.bancolombia.usecase.application.validation.PendingApplicationValidator;
import exception.BusinessRuleViolatedException;
import gateways.CustomLogger;
import gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.RoleTypes;

import java.util.UUID;

@RequiredArgsConstructor
public class ApplicationUseCase implements IApplicationUseCase {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusRepository applicationStatusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final TransactionalGateway transactionalGateway;
    private final AuthGateway authGateway;
    private final CustomLogger logger;
    private final IdentityRepository identityRepository;

    public Mono<ApplicationView> save(ApplicationView application) {

        logger.info("SaveApplication.start email={} id={} loanType={} status={}", application.getEmail(), application.getIdNumber(), application.getLoanType(), application.getStatus());
        Mono<UUID> statusIdMono = applicationStatusRepository.findIdByName(application.getStatus())
                .doOnSubscribe(s -> logger.debug("status.lookup.start status: statusId={}", application.getStatus()));
        Mono<UUID> loanTypeIdMono = loanTypeRepository.findIdByName(application.getLoanType())
                .doOnSubscribe(s ->logger.debug("loanType.lookup.start loanTypeId={}", application.getLoanType()))
                .switchIfEmpty(
                        Mono.error(
                                new BusinessRuleViolatedException("The loan type " + application.getLoanType() + " does not exist.")
                        )
                );

        Mono<Void> noDuplicatePending = PendingApplicationValidator.validate(
                application.getStatus().getName(),
                application.getIdNumber(),
                statusIdMono,
                applicationRepository
        ).doOnSubscribe(s -> logger.debug("noDuplicatePending.check.start id={} status={}", application.getIdNumber(), application.getStatus()))
                .doOnSuccess(v -> logger.debug("noDuplicatePending.check.ok id={}", application.getIdNumber()));;

        Mono<Void> authChecks = Mono.when(
                requireTrue(
                        authGateway.existsByEmail(application.getEmail())
                                .doOnSubscribe(s -> logger.debug("auth.existsByEmail.start email={}", application.getEmail())),
                        "The email does not belong to a user."
                ),
                requireTrue(
                        authGateway.existsByIdNumber(application.getIdNumber())
                                .doOnSubscribe(s -> logger.debug("auth.existsById.start id={}", application.getIdNumber())),
                        "The document does not belong to any user."
                )
        );

        Mono<Identity> identityMono = identityRepository.current().cache();

        Mono<Void> requesterIsClient = identityMono
                .map(id -> id.getRole() == RoleTypes.CLIENT)
                .flatMap(ok -> ok ? Mono.empty()
                        : Mono.error(new BusinessRuleViolatedException("Only Client users can create applications.")));

        Mono<Void> ownership = identityMono
                .map(id -> id.getEmail() != null
                && id.getEmail().equalsIgnoreCase(application.getEmail()))
                .flatMap(ok -> ok ? Mono.empty()
                        : Mono.error(new BusinessRuleViolatedException("You can only create applications for yousrself.")));

        Mono<Void> validations = Mono.when(
                noDuplicatePending,
                authChecks,
                requesterIsClient,
                ownership
        );

        return transactionalGateway.executeTransactional(
                validations.then(
                    Mono.zip(statusIdMono, loanTypeIdMono)
                    .map(tuple -> Application.builder()
                                .idNumber(application.getIdNumber())
                                .email(application.getEmail())
                                .amount(application.getAmount())
                                .duration(application.getDuration())
                                .statusId(tuple.getT1())
                                .loanTypeId(tuple.getT2())
                                .build()
                    )
                    .doOnSubscribe(s -> logger.debug("repository.save.start id={}", application.getIdNumber()))
                    .flatMap(applicationRepository::save)
                    .map(saved -> ApplicationView.builder()
                            .idNumber(saved.getIdNumber())
                            .email(saved.getEmail())
                            .amount(saved.getAmount())
                            .duration(saved.getDuration())
                            .status(application.getStatus())
                            .loanType(application.getLoanType())
                            .build()
                    )
                )
        )
        .doOnSuccess(s -> {
            logger.info("SaveApplication.ok email={} id={}", application.getEmail(), application.getIdNumber());
        });
    }

    private Mono<Void> requireTrue(Mono<Boolean> mono, String message) {
        return mono.flatMap(ok -> ok ?   Mono.empty() : Mono.error(new BusinessRuleViolatedException(message)));
    }

    private boolean hasApplicationFilters(ApplicationSearchFilters applicationSearchFilters) {
        return applicationSearchFilters.amount() != null
                || (applicationSearchFilters.email() != null && !applicationSearchFilters.email().isEmpty())
                || (applicationSearchFilters.duration() != null && applicationSearchFilters.duration() > 0)
                || (applicationSearchFilters.loanType() != null && !applicationSearchFilters.loanType().isEmpty())
                || applicationSearchFilters.interestRate() != null
                || (applicationSearchFilters.status() != null && !applicationSearchFilters.status().isEmpty());
    }

    private boolean hasUserFilters(UserSearchFilters userSearchFilters) {
        return (userSearchFilters.firstName() != null && !userSearchFilters.firstName().isEmpty())
        || (userSearchFilters.lastName() != null && !userSearchFilters.lastName().isEmpty())
        || (userSearchFilters.email() != null && !userSearchFilters.email().isEmpty())
        || (userSearchFilters.minBaseSalary() != null && userSearchFilters.minBaseSalary() > 0)
        || (userSearchFilters.maxBaseSalary() != null && userSearchFilters.maxBaseSalary() > 0);
    }

    public Flux<ApplicationList> findApplications(ApplicationSearchFilters applicationSearchFilters) {
        UserSearchFilters userSearchFilters = new UserSearchFilters(
                applicationSearchFilters.firstName(),
                applicationSearchFilters.lastName(),
                applicationSearchFilters.email(),
                null,null,null,null,null,
                applicationSearchFilters.minBaseSalary(),
                applicationSearchFilters.maxBaseSalary()
        );

        if (!hasUserFilters(userSearchFilters)) {
            return hasApplicationFilters(applicationSearchFilters)
                    ? applicationRepository.search(applicationSearchFilters)
                    : applicationRepository.getAllApplicationList();
        }

        return authGateway.search(userSearchFilters)
                .collectMap(UsersFiltered::email, u -> u)
                .flatMapMany(users -> {
                    if (users.isEmpty()) return Flux.empty();

                    return Flux.fromIterable(users.keySet())
                            .flatMap(email ->
                                    applicationRepository.search(new ApplicationSearchFilters(
                                            applicationSearchFilters.firstName(),
                                            applicationSearchFilters.lastName(),
                                            email,
                                            applicationSearchFilters.amount(),
                                            applicationSearchFilters.duration(),
                                            applicationSearchFilters.loanType(),
                                            applicationSearchFilters.interestRate(),
                                            applicationSearchFilters.status(),
                                            applicationSearchFilters.minBaseSalary(),
                                            applicationSearchFilters.maxBaseSalary()
                                    ))
                            ).map(app -> {
                                var u = users.get(app.getEmail());
                                var fullName = u.firstName() + " " + u.lastName();
                                var baseSalary = u.baseSalary();
                                return new ApplicationList(
                                    app.getAmount(),
                                    app.getDuration(),
                                    app.getEmail(),
                                    fullName,
                                    app.getLoanType(),
                                    app.getInteresRate(),
                                    app.getStatus(),
                                    baseSalary,
                                    app.getTotalApprovedMonthlyDebt()
                                );
                            });
                });
    }
}
