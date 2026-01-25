package pl.eticket.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.eticket.app.dto.event.*;
import pl.eticket.app.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventListResponse>> getEvents() {
        return ResponseEntity.ok(eventService.getAvailableEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventDetails(id));
    }

    @GetMapping("/{eventId}/sectors/{eventSectorId}/seats")
    public ResponseEntity<SectorSeatsResponse> getSectorSeats(@PathVariable Long eventId, @PathVariable Long eventSectorId) {
        return ResponseEntity.ok(eventService.getSectorSeats(eventId, eventSectorId));
    }
}
