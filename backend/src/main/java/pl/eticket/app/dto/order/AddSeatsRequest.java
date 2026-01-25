package pl.eticket.app.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AddSeatsRequest(
    @NotNull(message = "Event ID must be specified")
    Long eventId,

    @NotNull(message = "Event sector ID must be specified")
    Long eventSectorId,

    @NotNull(message = "Ticket type ID must be specified")
    Long ticketTypeId,

    @NotEmpty(message = "At least one seat must be selected")
    Set<Long> seatIds
) { }
