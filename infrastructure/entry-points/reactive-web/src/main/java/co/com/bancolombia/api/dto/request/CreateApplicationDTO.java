package co.com.bancolombia.api.dto.request;

import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;

public record CreateApplicationDTO (
    String email,
    BigDecimal amount,
    int duration,
    Long idNumber,
    String loanType
)
{}
