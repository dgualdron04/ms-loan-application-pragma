package co.com.bancolombia.model.application;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {
    private Long idNumber;

    private String email;

    private BigDecimal amount;

    private int duration;

    private UUID statusId;

    private UUID loanTypeId;


}
