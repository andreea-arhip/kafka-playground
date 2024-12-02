package org.example.api.client;

public record ClientRequest(
        String email,
        String firstName,
        String lastName
) { }
