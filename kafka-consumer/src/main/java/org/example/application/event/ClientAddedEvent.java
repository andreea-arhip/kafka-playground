package org.example.application.event;

public record ClientAddedEvent(
        String clientId,
        String email
) {
}
