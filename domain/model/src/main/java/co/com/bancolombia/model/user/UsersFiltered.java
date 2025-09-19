package co.com.bancolombia.model.user;

import utils.RoleTypes;

import java.time.LocalDate;

public record UsersFiltered(
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String idNumber,
        String phone,
        RoleTypes role,
        Integer baseSalary
) {
}
