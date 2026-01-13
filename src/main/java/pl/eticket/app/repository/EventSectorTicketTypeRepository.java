package pl.eticket.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.eticket.app.entity.EventSectorTicketType;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventSectorTicketTypeRepository extends JpaRepository<EventSectorTicketType, Long> {

    List<EventSectorTicketType> findByEventSectorId(Long eventSectorId);
    Optional<EventSectorTicketType> findByEventSectorIdAndTicketTypeId(Long eventSectorId, Long ticketTypeId);

    @Query("SELECT estt FROM EventSectorTicketType estt " +
            "JOIN FETCH estt.eventSector es JOIN FETCH es.event e JOIN FETCH e.venue v " +
            "JOIN FETCH v.address JOIN FETCH es.sector JOIN FETCH estt.ticketType WHERE estt.id = :id")
    Optional<EventSectorTicketType> findByIdWithFullDetails(Long id);
}