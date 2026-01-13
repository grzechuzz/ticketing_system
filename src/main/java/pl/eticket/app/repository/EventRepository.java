package pl.eticket.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.eticket.app.entity.Event;
import pl.eticket.app.entity.EventStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByStatus(EventStatus status);
    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = 'APPROVED' AND e.salesStart <= :now AND e.salesEnd >= :now")
    Page<Event> findAvailableForPurchase(Instant now, Pageable pageable);

    @Query("SELECT e FROM Event e JOIN FETCH e.venue v JOIN FETCH v.address JOIN FETCH e.organizer WHERE e.id = :id")
    Optional<Event> findByIdWithDetails(Long id);

    @Query("SELECT e FROM Event e JOIN FETCH e.eventSectors es JOIN FETCH es.sector WHERE e.id = :id")
    Optional<Event> findByIdWithSectors(Long id);
}