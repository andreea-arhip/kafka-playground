package org.example.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.example.api.client.ClientRequest;
import org.example.application.event.ClientAddedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final KafkaTemplate<String, ClientAddedEvent> kafkaTemplate;

    public void addClient(ClientRequest clientRequest) {

        ClientAddedEvent clientAddedEvent = ClientAddedEvent.builder()
                .clientId(Uuid.randomUuid().toString())
                .email(clientRequest.email())
                .build();
        log.info("Sending event {}", clientAddedEvent.clientId());
        kafkaTemplate.send("client-added", clientAddedEvent);
        log.info("Successfully sent event {}", clientAddedEvent.clientId());
    }
}
