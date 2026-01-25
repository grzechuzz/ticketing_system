package pl.eticket.app.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,
    String status,
    BigDecimal totalPriceNet,
    BigDecimal totalPriceGross,
    Instant reservedUntil,
    Instant createdAt,
    List<OrderItemResponse> items
) { }
