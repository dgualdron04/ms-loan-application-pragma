package co.com.bancolombia.model.application;

import utils.StatusType;

import java.util.UUID;

public record UpdateStatusApplication(
        String applicationId,
        String statusType
) {
}
