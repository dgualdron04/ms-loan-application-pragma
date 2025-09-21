package utils;

import exception.BusinessRuleViolatedException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusType {
    APPROVE("Aprobado"),
    NOT_APPROVE("No aprobado"),
    PENDING_REVIEW("Pendiente de revisión"),
    MANUAL_REVIEW("Revisión Manual");

    private final String name;

    public static StatusType fromName(String name) {
        return Arrays.stream(values())
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolatedException("The status does not exist: " + name));
    }
}
