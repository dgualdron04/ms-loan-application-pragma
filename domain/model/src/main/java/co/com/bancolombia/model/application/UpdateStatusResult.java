package co.com.bancolombia.model.application;

import utils.StatusType;

import java.util.UUID;

public record UpdateStatusResult(
        UUID id,
        StatusType statusType,
        String email
) {
}
