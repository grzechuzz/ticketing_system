package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.Event;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
        SELECT e FROM Event e JOIN FETCH e.venue v JOIN FETCH v.address 
        WHERE e.status = 'APPROVED' AND e.salesStart <= :now AND e.salesEnd >= :now 
        ORDER BY e.eventStart
        """)
    List<Event> findAvailableForSale(Instant now);

    @Query("""
        SELECT e FROM Event e JOIN FETCH e.venue v JOIN FETCH v.address 
        LEFT JOIN FETCH e.eventSectors es LEFT JOIN FETCH es.sector 
        WHERE e.id = :id
        """)
    Optional<Event> findByIdWithDetails(Long id);
}
