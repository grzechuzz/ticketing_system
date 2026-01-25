package pl.eticket.app.dto.event;

import java.util.List;

public record EventSectorInfo(
    Long eventSectorId,
    Long sectorId,
    String name,
    boolean standing,
    Integer availableCapacity,
    Integer totalCapacity,
    Integer rowsCount,
    Integer seatsPerRow,
    List<TicketPriceInfo> prices,
    Integer positionX,
    Integer positionY,
    Integer width,
    Integer height,
    String color
) { }
