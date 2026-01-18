package pl.eticket.app.dto.event;

import java.math.BigDecimal;

public record TicketPriceInfo(
    Long ticketTypeId,
    String ticketTypeName,
    BigDecimal priceNet,
    BigDecimal priceGross,
    BigDecimal vatRate
) { }
