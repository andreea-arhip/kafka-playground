package org.example.application;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientProcessor {

    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        final Map<String, String> serdeConfig = Collections.singletonMap(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        var keySpecificAvroSerde = Serdes.String();
        keySpecificAvroSerde.configure(serdeConfig, true);
        var valueSpecificAvroSerde = new SpecificAvroSerde<ClientAddedEvent>();
        valueSpecificAvroSerde.configure(serdeConfig, false);

        KStream<String, ClientAddedEvent> clientStream = streamsBuilder.stream(
                "client-added-topic",
                Consumed.with(keySpecificAvroSerde, valueSpecificAvroSerde)
        );

        KStream<String, String> colorStream = clientStream
                .mapValues((username, client) -> client.getFirstName() + " " + client.getLastName())
                .peek((username, client) -> log.info(client));

        colorStream.to("client-added-full-name-topic", Produced.with(Serdes.String(), Serdes.String()));
    }
}
