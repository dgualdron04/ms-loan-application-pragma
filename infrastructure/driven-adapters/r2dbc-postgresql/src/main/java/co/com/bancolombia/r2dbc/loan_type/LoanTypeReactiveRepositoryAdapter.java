package co.com.bancolombia.r2dbc.loan_type;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateways.LoanTypeRepository;
import co.com.bancolombia.r2dbc.entity.ApplicationStatusEntity;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import utils.StatusType;

import java.util.UUID;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    LoanType/* change for domain model */,
    LoanTypeEntity/* change for adapter model */,
    UUID,
    LoanTypeReactiveRepository
> implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, LoanType.class));
    }

    @Override
    public Mono<UUID> findIdByName(utils.LoanType loanType) {
        return repository.findByName(loanType.getName())
                .map(LoanTypeEntity::getId);
    }

}
