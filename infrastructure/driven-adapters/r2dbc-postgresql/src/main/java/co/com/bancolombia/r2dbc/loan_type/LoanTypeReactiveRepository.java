package co.com.bancolombia.r2dbc.loan_type;

import co.com.bancolombia.model.applicationstatus.ApplicationStatus;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.r2dbc.entity.ApplicationStatusEntity;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, UUID>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanTypeEntity> findByName(String name);
}
