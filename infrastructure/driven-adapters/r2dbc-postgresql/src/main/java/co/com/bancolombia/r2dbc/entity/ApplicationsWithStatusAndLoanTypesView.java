package co.com.bancolombia.r2dbc.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table("applications_with_status_and_loan_types") // nombre EXACTO de tu vista
public class ApplicationsWithStatusAndLoanTypesView {

    @Column("idnumber")
    private String idNumber;

    @Column("email")
    private String email;

    @Column("amount")
    private BigDecimal amount;

    @Column("duration")
    private Integer duration;

    @Column("loantype")
    private String loanType;

    @Column("interesrate")
    private Double interesRate;

    @Column("statustype")
    private String statustype;

    @Column("totalapprovedmonthlydebt")
    private BigDecimal totalApprovedMonthlyDebt;
}
