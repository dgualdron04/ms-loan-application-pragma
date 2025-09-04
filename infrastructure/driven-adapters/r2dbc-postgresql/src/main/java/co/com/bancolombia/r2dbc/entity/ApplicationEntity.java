package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table("applications")
public class ApplicationEntity {
    @Id
    @Column("application_id")
    private UUID id;

    @Column("idnumber")
    private Long idNumber;

    @Column("email")
    private String email;

    @Column("amount")
    private BigDecimal amount;

    @Column("duration")
    private int duration;

    @Column("status_id")
    private UUID statusId;

    @Column("loan_type_id")
    private UUID loanTypeId;
}
