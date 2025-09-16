package co.com.bancolombia.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.RoleTypes;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private Long idNumber;
    private String phone;
    private Integer baseSalary;
    private RoleTypes role;
}
