package co.com.bancolombia.model.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationList {
    private final BigDecimal amount;
    private final Integer duration;
    private final String email;
    private final String fullName;
    private final LoanType loanType;
    private final Double interesRate;
    private final StatusType status;
    private final Integer baseSalary;
    private final BigDecimal totalApprovedMonthlyDebt;
}
