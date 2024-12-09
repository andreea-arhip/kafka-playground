package org.example.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.example.api.client.ClientRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final KafkaTemplate<String, ClientAddedEvent> kafkaTemplate;

    public void addClient(ClientRequest clientRequest) {

        ClientAddedEvent clientAddedEvent = new ClientAddedEvent(
                Uuid.randomUuid().toString(),
                clientRequest.email(),
                clientRequest.firstName(),
                clientRequest.lastName()
                );
        log.info("Sending event {}", clientAddedEvent.getClientId());
        kafkaTemplate.send("client-added-topic", clientAddedEvent);
        log.info("Successfully sent event {}", clientAddedEvent.getClientId());
    }
}
