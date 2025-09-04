package utils;

import exception.BusinessRuleViolatedException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoanType {
    PERSONAL("Personal"),
    MORTGAGE("Hipotecario"),
    EDUCATIONAL("Educativo"),
    CONSUMER("Consumo"),
    BUSINESS("Empresarial"),
    MICROCREDIT("Microcredito");

    private final String name;

    public static LoanType fromName(String name) {
        return Arrays.stream(values())
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolatedException("The loan type does not exist: " + name));
    }
}
