package pl.eticket.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eticket.app.dto.ticket.TicketResponse;
import pl.eticket.app.entity.Ticket;
import pl.eticket.app.exception.ApiException;
import pl.eticket.app.mapper.TicketMapper;
import pl.eticket.app.repository.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Transactional(readOnly = true)
    public List<TicketResponse> getUserTicketsList(Long userId) {
        return ticketRepository.findAllByUserId(userId).stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long userId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> ApiException.notFound("Ticket not found"));

        if (!ticket.getOrderItem().getOrder().getUser().getId().equals(userId)) {
            throw ApiException.forbidden("Access denied");
        }

        return ticketMapper.toResponse(ticket);
    }
}
