package co.com.bancolombia.model.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationView {
    private final Long idNumber;

    private final String email;

    private final BigDecimal amount;

    private final int duration;

    private final StatusType status;

    private final LoanType loanType;
}
