package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Ticket;
import pl.eticket.app.entity.TicketStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByCode(UUID code);
    List<Ticket> findByOrderItemId(Long orderItemId);

    @Query("SELECT t FROM Ticket t JOIN t.orderItem oi JOIN oi.order o WHERE o.user.id = :userId AND t.status = :status")
    List<Ticket> findByUserIdAndStatus(Long userId, TicketStatus status);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.orderItem WHERE t.code = :code")
    Optional<Ticket> findByCodeWithDetails(UUID code);

    @Query("SELECT t FROM Ticket t JOIN t.orderItem oi JOIN oi.order o WHERE o.user.id = :userId ORDER BY oi.eventDateSnapshot DESC")
    List<Ticket> findAllByUserId(Long userId);
}