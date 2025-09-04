package co.com.bancolombia.model.loantype.gateways;

import reactor.core.publisher.Mono;
import utils.LoanType;
import utils.StatusType;

import java.util.UUID;

public interface LoanTypeRepository {
    Mono<UUID> findIdByName(LoanType loan);
}
