package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.CreateApplicationDTO;
import co.com.bancolombia.api.dto.response.ApplicationResponseDTO;
import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.ApplicationView;
import org.mapstruct.Mapper;
import utils.LoanType;
import utils.StatusType;

import java.math.BigDecimal;

@Mapper(componentModel="spring")
public class ApplicationApiMapper {
    public ApplicationView toDomain(CreateApplicationDTO dto) {
        return new ApplicationView(
                dto.idNumber(),
                dto.email(),
                dto.amount(),
                dto.duration(),
                StatusType.PENDING_REVIEW,
                LoanType.fromName(dto.loanType())
        );
    }

    public ApplicationResponseDTO toResponse(ApplicationView view) {
        return new ApplicationResponseDTO(
                view.getEmail(),
                view.getAmount(),
                view.getDuration(),
                view.getIdNumber(),
                view.getStatus(),
                view.getLoanType()
        );
    }
}