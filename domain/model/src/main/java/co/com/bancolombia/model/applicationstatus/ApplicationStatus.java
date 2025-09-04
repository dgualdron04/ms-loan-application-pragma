package co.com.bancolombia.model.applicationstatus;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.StatusType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationStatus {
    private StatusType name;
    private String description;
}
