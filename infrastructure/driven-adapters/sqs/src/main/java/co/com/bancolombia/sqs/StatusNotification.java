package co.com.bancolombia.sqs;

import co.com.bancolombia.model.notification.ApplicationStatusNotification;
import co.com.bancolombia.model.notification.gateways.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateways.CustomLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class StatusNotification implements NotificationRepository {

    private final SqsAsyncClient asyncClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${adapter.sqs.queue-url}")
    private String queueUrl;
    private final CustomLogger logger;

    public Mono<Void> publish(ApplicationStatusNotification event) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(event))
                .doOnNext(json -> logger.info("Enviando a SQS " + json))
                .flatMap(json -> Mono.fromFuture(
                        asyncClient.sendMessage(SendMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .messageBody(json)
                                .build())
                ))
                .doOnNext(resp -> logger.info("Mensaje SQS enviado, id = " + resp.messageId()))
                .then();
    }

}
