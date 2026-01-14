package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eticket.app.entity.TicketType;
import java.util.Optional;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    Optional<TicketType> findByName(String name);
}