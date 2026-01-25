package pl.eticket.app.dto.event;

import java.time.Instant;
import java.util.List;

public record EventDetailResponse(
    Long id,
    String name,
    String description,
    Instant eventStart,
    Instant eventEnd,
    Instant salesStart,
    Instant salesEnd,
    Integer maxTicketsPerCustomer,
    Integer minAge,
    String coverImageUrl,
    boolean saleActive,
    VenueInfo venue,
    List<EventSectorInfo> sectors
) {}
