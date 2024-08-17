package org.zero.paymentservice.model.kafka;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Event {
    private final String eventType;
    private String eventId = UUID.randomUUID().toString();
    private Instant eventTime = Instant.now();
    private final Integer eventVersion;
}
