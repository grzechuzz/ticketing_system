package pl.eticket.app.dto.event;

import java.time.Instant;

public record EventListResponse(
    Long id,
    String name,
    String description,
    Instant eventStart,
    Instant eventEnd,
    String venueName,
    String venueCity,
    String coverImageUrl,
    boolean saleActive
) { }