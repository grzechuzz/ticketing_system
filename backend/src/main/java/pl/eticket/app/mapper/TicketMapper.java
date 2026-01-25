package pl.eticket.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.eticket.app.dto.ticket.TicketResponse;
import pl.eticket.app.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "code", expression = "java(ticket.getCode().toString())")
    @Mapping(target = "status", expression = "java(ticket.getStatus().name())")
    @Mapping(target = "eventName", source = "orderItem.eventNameSnapshot")
    @Mapping(target = "eventDate", source = "orderItem.eventDateSnapshot")
    @Mapping(target = "venueName", source = "orderItem.venueNameSnapshot")
    @Mapping(target = "venueAddress", source = "orderItem.venueAddressSnapshot")
    @Mapping(target = "sectorName", source = "orderItem.sectorNameSnapshot")
    @Mapping(target = "row", source = "orderItem.rowSnapshot")
    @Mapping(target = "seatNumber", source = "orderItem.seatNumberSnapshot")
    @Mapping(target = "ticketTypeName", source = "orderItem.ticketTypeNameSnapshot")
    @Mapping(target = "priceGross", source = "orderItem.unitPriceGross")
    TicketResponse toResponse(Ticket ticket);
}
