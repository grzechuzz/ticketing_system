package pl.eticket.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.eticket.app.dto.ticket.TicketResponse;
import pl.eticket.app.security.SecurityUser;
import pl.eticket.app.service.PdfTicketService;
import pl.eticket.app.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final PdfTicketService pdfTicketService;

    @GetMapping
    public List<TicketResponse> getTickets(@AuthenticationPrincipal SecurityUser user) {
        return ticketService.getUserTicketsList(user.id());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id, @AuthenticationPrincipal SecurityUser user) {
        return ticketService.getTicket(user.id(), id);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long id) {
        byte[] pdf = pdfTicketService.generateTicketPdf(user.id(), id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
