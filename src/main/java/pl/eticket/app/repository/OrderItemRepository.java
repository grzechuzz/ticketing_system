package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.OrderItem;
import java.util.List;
import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi.seat.id FROM OrderItem oi " +
            "WHERE oi.seat IS NOT NULL " +
            "AND oi.seat.sector.id = :sectorId " +
            "AND oi.order.event.id = :eventId " +
            "AND oi.order.status IN ('PENDING', 'AWAITING_PAYMENT', 'COMPLETED')")
    Set<Long> findOccupiedSeatIdsForEvent(Long eventId, Long sectorId);

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END FROM OrderItem oi " +
            "WHERE oi.seat.id = :seatId " +
            "AND oi.order.event.id = :eventId " +
            "AND oi.order.status IN ('PENDING', 'AWAITING_PAYMENT', 'COMPLETED')")
    boolean isSeatOccupied(Long seatId, Long eventId);
}