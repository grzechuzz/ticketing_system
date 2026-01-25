package pl.eticket.app.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddStandingTicketsRequest(
    @NotNull(message = "Event ID is required")
    Long eventId,

    @NotNull(message = "Event sector ID is required")
    Long eventSectorId,

    @NotNull(message = "Ticket type ID is required")
    Long ticketTypeId,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "At least one ticket must be added")
    Integer quantity
) { }
