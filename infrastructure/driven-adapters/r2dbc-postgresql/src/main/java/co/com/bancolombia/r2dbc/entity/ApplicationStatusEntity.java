package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import utils.StatusType;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("applications_status")
public class ApplicationStatusEntity {
    @Id
    @Column("application_status_id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
