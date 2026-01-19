package pl.eticket.app.dto.order;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderItemResponse(
    Long id,
    Long eventId,
    String eventName,
    Instant eventDate,
    String sectorName,
    String ticketTypeName,
    Integer row,
    Integer seatNumber,
    Integer quantity,
    BigDecimal unitPriceNet,
    BigDecimal unitPriceGross,
    BigDecimal totalPriceGross
) { }
