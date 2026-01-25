package pl.eticket.app.dto.ticket;

import java.math.BigDecimal;
import java.time.Instant;

public record TicketResponse(
    Long id,
    String code,
    String status,
    String eventName,
    Instant eventDate,
    String venueName,
    String venueAddress,
    String sectorName,
    Integer row,
    Integer seatNumber,
    String ticketTypeName,
    BigDecimal priceGross,
    Instant createdAt
) { }
