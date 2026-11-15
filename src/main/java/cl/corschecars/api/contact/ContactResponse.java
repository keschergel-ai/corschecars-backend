package cl.corschecars.api.contact;

import java.time.Instant;

public record ContactResponse(Long id, Instant receivedAt) {
}
