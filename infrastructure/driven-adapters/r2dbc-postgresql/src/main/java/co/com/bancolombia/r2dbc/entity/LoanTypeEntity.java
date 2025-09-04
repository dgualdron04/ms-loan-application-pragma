package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import utils.LoanType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table("loans_types")
public class LoanTypeEntity {
    @Id
    @Column("loan_type_id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("minimumamount")
    private BigDecimal minimumAmount;

    @Column("maximumamount")
    private BigDecimal maximumAmount;

    @Column("interesrate")
    private Double interesRate;

    @Column("automaticvalidation")
    private Boolean automaticValidation;
}
