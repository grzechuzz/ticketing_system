package pl.eticket.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eticket.app.dto.event.*;
import pl.eticket.app.entity.Event;
import pl.eticket.app.entity.EventSector;
import pl.eticket.app.entity.Seat;
import pl.eticket.app.entity.Sector;
import pl.eticket.app.exception.ApiException;
import pl.eticket.app.mapper.EventMapper;
import pl.eticket.app.repository.EventRepository;
import pl.eticket.app.repository.EventSectorRepository;
import pl.eticket.app.repository.OrderItemRepository;
import pl.eticket.app.repository.SeatRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventSectorRepository eventSectorRepository;
    private final SeatRepository seatRepository;
    private final OrderItemRepository orderItemRepository;
    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<EventListResponse> getAvailableEvents() {
        return eventRepository.findAvailableForSale(Instant.now()).stream()
                .map(eventMapper::toEventListResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventDetailResponse getEventDetails(Long eventId) {
        Event event = eventRepository.findByIdWithDetails(eventId)
                .orElseThrow(() -> ApiException.notFound("Event not found"));

        List<EventSector> sectors = eventSectorRepository.findByEventIdWithDetails(eventId);

        return eventMapper.toDetailResponse(event, sectors);
    }

    @Transactional(readOnly = true)
    public SectorSeatsResponse getSectorSeats(Long eventId, Long eventSectorId) {
        EventSector eventSector = eventSectorRepository.findById(eventSectorId)
                .orElseThrow(() -> ApiException.notFound("Sector not found"));

        if (!eventSector.getEvent().getId().equals(eventId)) {
            throw ApiException.badRequest("Sector does not belong to the specified event");
        }

        Sector sector = eventSector.getSector();
        if (sector.getIsStanding()) {
            throw ApiException.badRequest("Sector is not a seating sector");
        }

        Set<Long> occupiedSeatIds = orderItemRepository.findOccupiedSeatIdsForEvent(eventId, sector.getId());
        List<Seat> seats = seatRepository.findBySectorIdOrderByRowNumberAscSeatNumberAsc(sector.getId());

        List<RowInfo> rows = seats.stream()
                .collect(Collectors.groupingBy(Seat::getRowNumber, TreeMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> {
                    Integer rowNum = entry.getKey();
                    List<Seat> rowSeats = entry.getValue();
                    List<SeatInfo> seatInfos = rowSeats.stream()
                            .map(seat -> eventMapper.toSeatInfo(seat, occupiedSeatIds.contains(seat.getId())))
                            .toList();

                    return eventMapper.toRowInfo(rowNum, seatInfos);
                })
                .toList();

        return new SectorSeatsResponse(
                eventSectorId,
                sector.getName(),
                sector.getRowsCount(),
                sector.getSeatsPerRow(),
                rows
        );
    }
}
