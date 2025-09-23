package co.com.bancolombia.model.notification.gateways;

import co.com.bancolombia.model.notification.ApplicationStatusNotification;
import reactor.core.publisher.Mono;

public interface NotificationRepository {
    Mono<Void> publish(ApplicationStatusNotification event);
}
