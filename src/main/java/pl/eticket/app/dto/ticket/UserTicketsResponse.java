package pl.eticket.app.dto.ticket;

import java.time.Instant;
import java.util.List;

public record UserTicketsResponse(
    List<EventTickets> events
) {
    public record EventTickets(
        Long eventId,
        String eventName,
        Instant eventDate,
        String venueName,
        String venueAddress,
        List<TicketResponse> tickets
    ) {}
}
