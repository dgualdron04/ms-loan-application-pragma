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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import utils.LoanType;
import utils.RoleTypes;
import utils.StatusType;
import utils.pagination.PageOptions;
import utils.pagination.PageResult;
import utils.pagination.SortOrder;

import java.util.*;

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
        Criteria c = buildCriteria(applicationSearchFilters, null);
        Query q = Query.query(c);
        return template.select(q, ApplicationsWithStatusAndLoanTypesView.class)
                .map(a -> new ApplicationList(
                        a.getAmount(),
                        a.getDuration(),
                        a.getEmail(),
                        null,
                        LoanType.fromName(a.getLoanType()),
                        a.getInteresRate(),
                        StatusType.fromName(a.getStatusType()),
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
                        StatusType.fromName(a.getStatusType()),
                        null,
                        a.getTotalApprovedMonthlyDebt()
                ));
    }

    @Override
    public Mono<PageResult<ApplicationList>> searchPaged(ApplicationSearchFilters applicationSearchFilters, PageOptions pageOptions, Collection<String> allowedEmails) {
        Criteria criteria = buildCriteria(applicationSearchFilters, allowedEmails);

        return pageQuery(criteria, pageOptions);
    }

    @Override
    public Mono<PageResult<ApplicationList>> getAllApplicationListPaged(PageOptions pageOptions) {
        return pageQuery(Criteria.empty(), pageOptions);
    }

    private Mono<PageResult<ApplicationList>> pageQuery(Criteria criteria, PageOptions pageOptions) {

        Sort sort = toSort(pageOptions.sort());
        Pageable pageable =
                PageRequest.of(
                        Math.max(pageOptions.page(), 0),
                        Math.max(pageOptions.size(), 1),
                        sort
                );

        Query base = Query.query(criteria);
        Query basePageable = base.with(pageable);

        Mono<Long> totalMono = template.count(base, ApplicationsWithStatusAndLoanTypesView.class);

        Mono<List<ApplicationList>> itemsMono = template.select(basePageable, ApplicationsWithStatusAndLoanTypesView.class)
                .map(u -> new ApplicationList(
                        u.getAmount(),
                        u.getDuration(),
                        u.getEmail(),
                        null,
                        LoanType.fromName(u.getLoanType()),
                        u.getInteresRate(),
                        StatusType.fromName(u.getStatusType()),
                        null,
                        u.getTotalApprovedMonthlyDebt()
                ))
                .collectList();

        return Mono.zip(totalMono, itemsMono)
                .map(t -> {
                    long total = t.getT1();
                    List<ApplicationList> items = t.getT2();
                    boolean hasNext = ((long) (pageOptions.page() + 1) * pageOptions.size()) < total;
                    return new PageResult<>(items, total, pageOptions.page(), pageOptions.size(), hasNext);
                });
    }

    private Sort toSort(List<SortOrder> sortOrders) {
        Map<String, String> mapColumns = Map.of(
                "email", "email",
                "duration", "duration",
                "amount", "amount",
                "loanType", "loantype",
                "interesRate", "interesrate",
                "statusType", "statustype"
        );

        ArrayList<Sort.Order> list = new ArrayList<Sort.Order>();
        for (SortOrder sortOrder : sortOrders) {
            String col = mapColumns.get(sortOrder.field());
            if (col != null) {
                list.add(sortOrder.asc()
                        ? Sort.Order.asc(col)
                        : Sort.Order.desc(col));
            }
        }

        return list.isEmpty()
                ? Sort.by("email")
                : Sort.by(list);
    }

    private Criteria buildCriteria(ApplicationSearchFilters u, Collection<String> allowedEmails) {
        List<Criteria> list = new ArrayList<>();
        if (allowedEmails != null && !allowedEmails.isEmpty())
            list.add(Criteria.where("email").in(allowedEmails));
        else if (u.email() != null && !u.email().isBlank())
            list.add(Criteria.where("email").like("%" + u.email().trim() + "%").ignoreCase(true));
        if (u.duration() != null && u.duration() > 0)
            list.add(Criteria.where("duration").is(u.duration()));
        if (u.amount() != null)
                list.add(Criteria.where("amount").is(u.amount()));
        if (u.loanType() != null && !u.loanType().isBlank())
            list.add(Criteria.where("loantype").is(u.loanType()));
        if (u.interestRate() != null)
            list.add(Criteria.where("interesrate").is(u.interestRate()));
        if (u.status() != null && !u.status().isEmpty())
            list.add(Criteria.where("statustype").is(u.status()));
        return list.isEmpty() ? Criteria.empty() : Criteria.from(list.toArray(new Criteria[0]));
    }
}
