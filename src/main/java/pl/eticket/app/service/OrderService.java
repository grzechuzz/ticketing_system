package pl.eticket.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eticket.app.dto.order.*;
import pl.eticket.app.dto.ticket.TicketResponse;
import pl.eticket.app.entity.*;
import pl.eticket.app.exception.ApiException;
import pl.eticket.app.mapper.OrderMapper;
import pl.eticket.app.mapper.TicketMapper;
import pl.eticket.app.repository.*;
import pl.eticket.app.validation.SeatGapValidator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EventRepository eventRepository;
    private final EventSectorRepository eventSectorRepository;
    private final EventSectorTicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final SeatGapValidator seatGapValidator;
    private final OrderMapper orderMapper;
    private final SeatRepository seatRepository;
    private final TicketMapper ticketMapper;

    private Order getOrCreateCart(User user) {
        return orderRepository.findActiveCartByUser(user.getId())
                .orElseGet(() -> {
                    Order newOrder = Order.create(user);
                    return orderRepository.save(newOrder);
                });
    }

    @Transactional
    public AddSeatsResponse addSeats(Long userId, AddSeatsRequest addSeatsRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        eventRepository.findById(addSeatsRequest.eventId())
                .orElseThrow(() -> ApiException.notFound("Event not found"));

        EventSector eventSector = eventSectorRepository.findById(addSeatsRequest.eventSectorId())
                .orElseThrow(() -> ApiException.notFound("Event sector not found"));

        if (eventSector.isStanding()) {
            throw ApiException.badRequest("Event sector is not seated");
        }

        EventSectorTicketType ticketType = ticketTypeRepository
                .findByEventSectorIdAndTicketTypeIdWithDetails(addSeatsRequest.eventSectorId(), addSeatsRequest.ticketTypeId())
                .orElseThrow(() -> ApiException.notFound("Ticket type not found in the specified event sector"));

        List<Seat> seats = seatRepository.findAllById(addSeatsRequest.seatIds());
        if (seats.size() != addSeatsRequest.seatIds().size()) {
            throw ApiException.badRequest("Some seats don't exist");
        }

        Long sectorId = eventSector.getSector().getId();
        for (Seat seat : seats) {
            if (!seat.getSector().getId().equals(sectorId)) {
                throw ApiException.badRequest("All seats should belong to the same sector");
            }
        }

        Set<Long> occupiedSeatIds = orderItemRepository.findOccupiedSeatIdsForEvent(addSeatsRequest.eventId(),  sectorId);
        for (Long seatId : addSeatsRequest.seatIds()) {
            if (occupiedSeatIds.contains(seatId)) {
                throw ApiException.badRequest("Seat is already occupied: " + seatId);
            }
        }

        List<Seat> allSeatsInSector = seatRepository.findAllBySectorId(sectorId);
        SeatValidationResult validationResult = seatGapValidator.validate(allSeatsInSector, occupiedSeatIds, addSeatsRequest.seatIds());

        if (!validationResult.valid()) {
            Order existingOrder = orderRepository.findActiveCartByUser(userId).orElse(null);
            OrderResponse orderResponse = existingOrder != null ? orderMapper.toResponse(existingOrder) : null;
            return AddSeatsResponse.validationFailed(orderResponse, validationResult);
        }

        Order order = getOrCreateCart(user);

        for (Seat seat : seats) {
            OrderItem orderItem = OrderItem.createForSeat(order, ticketType, seat);
            order.addItem(orderItem);
        }
        order = orderRepository.save(order);

        return AddSeatsResponse.success(orderMapper.toResponse(order));
    }

    @Transactional
    public OrderResponse addStandingTickets(Long userId, AddStandingTicketsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        eventRepository.findById(request.eventId())
                .orElseThrow(() -> ApiException.notFound("Event not found"));

        EventSector eventSector = eventSectorRepository.findByIdForUpdate(request.eventSectorId())
                .orElseThrow(() -> ApiException.notFound("Event sector not found"));

        if (!eventSector.isStanding()) {
            throw ApiException.badRequest("Event sector is not standing");
        }

        EventSectorTicketType ticketType = ticketTypeRepository
                .findByEventSectorIdAndTicketTypeIdWithDetails(request.eventSectorId(), request.ticketTypeId())
                .orElseThrow(() -> ApiException.notFound("Ticket type not found in the specified event sector"));

        int updated = eventSectorRepository.decrementCapacity(request.eventSectorId(), request.quantity());

        if (updated == 0) {
            throw ApiException.conflict("Not enough tickets available");
        }

        Order order = getOrCreateCart(user);
        OrderItem orderItem = OrderItem.createForStanding(order, ticketType, request.quantity());
        order.addItem(orderItem);
        order = orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getCurrentCart(Long userId) {
        return orderRepository.findActiveCartByUser(userId)
                .map(orderMapper::toResponse)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdForUpdateWithItems(orderId)
                .orElseThrow(() -> ApiException.notFound("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        return orderMapper.toResponse(order);
    }

    @Transactional
    public CheckoutResponse checkout(Long userId, Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> ApiException.notFound("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        if (!order.canCheckout()) {
            if (order.isExpired()) {
                throw ApiException.badRequest("Order has expired");
            }
            if (order.getItems().isEmpty()) {
                throw ApiException.badRequest("Order is empty");
            }
            throw ApiException.badRequest("Order cannot be checked out");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(Instant.now());

        List<TicketResponse> ticketResponses = new ArrayList<>();

        for (OrderItem orderItem : order.getItems()) {
            for (int i = 0; i < orderItem.getQuantity(); i++) {
                Ticket ticket = Ticket.create(orderItem);
                orderItem.addTicket(ticket);
                ticketRepository.save(ticket);
                ticketResponses.add(ticketMapper.toResponse(ticket));
            }
        }

        orderRepository.save(order);

        return CheckoutResponse.success(orderId, ticketResponses);
    }

    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> ApiException.notFound("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw ApiException.badRequest("Cannot delete a completed order");
        }

        for (OrderItem orderItem : order.getItems()) {
            if (orderItem.getSeat() == null) {
                eventSectorRepository.incrementCapacity(
                        orderItem.getEventSectorTicketType().getEventSector().getId(),
                        orderItem.getQuantity()
                );
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Transactional
    public OrderResponse removeItem(Long userId, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> ApiException.notFound("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw ApiException.badRequest("Can only modify pending orders");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> ApiException.notFound("Order item not found"));

        if (item.getSeat() == null) {
            eventSectorRepository.incrementCapacity(
                    item.getEventSectorTicketType().getEventSector().getId(),
                    item.getQuantity()
            );
        }

        order.getItems().remove(item);
        order.recalculateTotals();
        orderRepository.save(order);

        return orderMapper.toResponse(order);
    }
}
