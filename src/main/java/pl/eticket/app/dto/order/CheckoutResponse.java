package pl.eticket.app.dto.order;

import pl.eticket.app.dto.ticket.TicketResponse;

import java.util.List;

public record CheckoutResponse(
    Long orderId,
    boolean success,
    String message,
    List<TicketResponse> tickets
) {
    public static CheckoutResponse success(Long orderId, List<TicketResponse> tickets) {
        return new CheckoutResponse(orderId, true, "Payment successful. Tickets generated.", tickets);
    }

    public static CheckoutResponse failure(Long orderId, String message) {
        return new CheckoutResponse(orderId, false, message, List.of());
    }
}
