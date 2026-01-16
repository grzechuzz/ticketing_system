package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.TicketHolder;

import java.util.Optional;

public interface TicketHolderRepository extends JpaRepository<TicketHolder, Long> {
    Optional<TicketHolder> findByTicketId(Long ticketId);
}g