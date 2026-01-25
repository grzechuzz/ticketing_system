package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.eticket.app.entity.EventSector;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface EventSectorRepository extends JpaRepository<EventSector, Long> {

    List<EventSector> findByEventId(Long eventId);
    Optional<EventSector> findByEventIdAndSectorId(Long eventId, Long sectorId);

    @Query("SELECT es FROM EventSector es JOIN FETCH es.sector JOIN FETCH es.ticketTypes tt JOIN FETCH tt.ticketType WHERE es.event.id = :eventId")
    List<EventSector> findByEventIdWithDetails(Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT es FROM EventSector es WHERE es.id = :id")
    Optional<EventSector> findByIdForUpdate(Long id);

    @Modifying
    @Query("UPDATE EventSector es SET es.availableCapacity = es.availableCapacity - :qty WHERE es.id = :id AND es.availableCapacity >= :qty")
    int decrementCapacity(Long id, int qty);

    @Modifying
    @Query("UPDATE EventSector es SET es.availableCapacity = LEAST(es.availableCapacity + :qty, es.capacitySnapshot) WHERE es.id = :id")
    int incrementCapacity(Long id, int qty);
}