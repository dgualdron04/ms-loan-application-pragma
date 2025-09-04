package co.com.bancolombia.model.loantype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    private utils.LoanType name;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private Double interesRate;
    private Boolean automaticValidation;
}
