package co.com.bancolombia.api.dto.response;

import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;
import java.util.UUID;

public record ApplicationResponseDTO(
    String email,
    BigDecimal amount,
    int duration,
    Long idNumber,
    StatusType status,
    LoanType loanType
) {}
