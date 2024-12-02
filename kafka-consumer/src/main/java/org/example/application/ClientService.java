package org.example.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    @KafkaListener(topics = "client-added")
    public void listen(ClientAddedEvent clientAddedEvent) {
        log.info("Got message from topic: {} ", clientAddedEvent);
    }
}
