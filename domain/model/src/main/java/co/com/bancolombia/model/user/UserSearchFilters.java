package co.com.bancolombia.model.user;

import java.time.LocalDate;

public record UserSearchFilters(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDateFrom,
        LocalDate birthDateTo,
        String idNumber,
        String phone,
        String roleName,
        Integer minBaseSalary,
        Integer maxBaseSalary
) {
}
