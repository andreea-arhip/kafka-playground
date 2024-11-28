package org.example.application.event;

import lombok.Builder;

@Builder
public record ClientAddedEvent (
    String clientId,
    String email
){}
