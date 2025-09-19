package co.com.bancolombia.model.application;

import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;

public record ApplicationSearchFilters(
        String firstName,
        String lastName,
        String email,
        BigDecimal amount,
        Integer duration,
        String loanType,
        Double interestRate,
        String status,
        Integer minBaseSalary,
        Integer maxBaseSalary
) { }
