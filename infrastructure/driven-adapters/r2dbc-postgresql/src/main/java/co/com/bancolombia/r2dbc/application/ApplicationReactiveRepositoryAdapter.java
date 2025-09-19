package co.com.bancolombia.r2dbc.application;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationList;
import co.com.bancolombia.model.application.ApplicationSearchFilters;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.r2dbc.application.view.ApplicationViewReactiveRepository;
import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import co.com.bancolombia.r2dbc.entity.ApplicationsWithStatusAndLoanTypesView;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.LoanType;
import utils.StatusType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Application/* change for domain model */,
    ApplicationEntity/* change for adapter model */,
    UUID,
    ApplicationReactiveRepository
> implements ApplicationRepository {

    public ApplicationReactiveRepositoryAdapter(ApplicationReactiveRepository repository, ObjectMapper mapper, ApplicationViewReactiveRepository viewRepo, R2dbcEntityTemplate template) {
        super(repository, mapper, entity -> mapper.map(entity, Application.class));
        this.viewRepo = viewRepo;
        this.template = template;
    }

    private final ApplicationViewReactiveRepository viewRepo;
    private final R2dbcEntityTemplate template;

    @Override
    public Mono<Application> save(Application application) {
        return super.save(application);
    }

    public Mono<Boolean> existsByIdNumberAndStatusId(Long idNumber, UUID statusId) {
        return repository.existsByIdNumberAndStatusId(idNumber, statusId);
    }

    public Flux<ApplicationList> search(ApplicationSearchFilters applicationSearchFilters) {
        Criteria c = buildCriteria(applicationSearchFilters);
        Query q = Query.query(c);
        return template.select(q, ApplicationsWithStatusAndLoanTypesView.class)
                .map(a -> new ApplicationList(
                        a.getAmount(),
                        a.getDuration(),
                        a.getEmail(),
                        null,
                        LoanType.fromName(a.getLoanType()),
                        a.getInteresRate(),
                        StatusType.fromName(a.getStatustype()),
                        null,
                        a.getTotalApprovedMonthlyDebt()
                ));
    }

    public Flux<ApplicationList> getAllApplicationList() {
        return viewRepo.findAll()
                .map(a -> new ApplicationList(
                        a.getAmount(),
                        a.getDuration(),
                        a.getEmail(),
                        null,
                        LoanType.fromName(a.getLoanType()),
                        a.getInteresRate(),
                        StatusType.fromName(a.getStatustype()),
                        null,
                        a.getTotalApprovedMonthlyDebt()
                ));
    }

    private Criteria buildCriteria(ApplicationSearchFilters u) {
        List<Criteria> list = new ArrayList<>();
        if (u.email() != null && !u.email().isEmpty())
            list.add(Criteria.where("email").like("%" + u.email().trim() + "%").ignoreCase(true));
        if (u.duration() != null && u.duration() > 0)
            list.add(Criteria.where("duration").is(u.duration()));
        if (u.amount() != null)
                list.add(Criteria.where("amount").is(u.amount()));
        if (u.loanType() != null && !u.loanType().isEmpty())
            list.add(Criteria.where("loantype").is(u.loanType()));
        if (u.interestRate() != null)
            list.add(Criteria.where("interesrate").is(u.interestRate()));
        if (u.status() != null && !u.status().isEmpty())
            list.add(Criteria.where("statustype").is(u.status()));
        return list.isEmpty() ? Criteria.empty() : Criteria.from(list.toArray(new Criteria[0]));
    }
}
