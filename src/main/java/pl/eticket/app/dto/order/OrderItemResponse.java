package pl.eticket.app.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    String sectorName,
    String ticketTypeName,
    Integer row,
    Integer seatNumber,
    Integer quantity,
    BigDecimal unitPriceNet,
    BigDecimal unitPriceGross,
    BigDecimal totalPriceGross
) { }
