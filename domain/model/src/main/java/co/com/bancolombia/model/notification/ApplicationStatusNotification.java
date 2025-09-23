package co.com.bancolombia.model.notification;

import lombok.Builder;

@Builder
public record ApplicationStatusNotification(
        String applicationId,
        String email,
        String status,
        String timestamp
) {
}
