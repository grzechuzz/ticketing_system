package pl.eticket.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.eticket.app.dto.order.*;
import pl.eticket.app.security.SecurityUser;
import pl.eticket.app.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/seats")
    public ResponseEntity<AddSeatsResponse> addSeats(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody AddSeatsRequest addSeatsRequest
    ) {
        AddSeatsResponse response = orderService.addSeats(user.id(), addSeatsRequest);

        if (!response.validation().valid()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/standing")
    public ResponseEntity<OrderResponse> addStandingTickets(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody AddStandingTicketsRequest request
    ) {
        OrderResponse response = orderService.addStandingTickets(user.id(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart/current")
    public ResponseEntity<OrderResponse> getCurrentCart(
            @AuthenticationPrincipal SecurityUser user
    ) {
        OrderResponse response = orderService.getCurrentCart(user.id());
        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id
    ) {
        CheckoutResponse response = orderService.checkout(user.id(), id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id
    ) {
        OrderResponse response = orderService.getOrder(user.id(), id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id
    ) {
        orderService.deleteOrder(user.id(), id);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderResponse> removeItem(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        OrderResponse response = orderService.removeItem(user.id(), orderId, itemId);
        return ResponseEntity.ok(response);
    }
}
