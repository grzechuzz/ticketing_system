package pl.eticket.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.eticket.app.dto.event.*;
import pl.eticket.app.entity.*;

import java.util.List;

@Mapper(componentModel = "spring", imports = {java.time.Instant.class})
public interface EventMapper {

    @Mapping(target = "venueName", source = "venue.name")
    @Mapping(target = "venueCity", source = "venue.address.city")
    @Mapping(target = "saleActive", expression = "java(event.isSalesOpen(Instant.now()))")
    EventListResponse toEventListResponse(Event event);

    @Mapping(target = "saleActive", expression = "java(event.isSalesOpen(Instant.now()))")
    @Mapping(target = "sectors", source = "sectors")
    EventDetailResponse toDetailResponse(Event event, List<EventSector> sectors);

    @Mapping(target = "address", source = "address.fullAddress")
    VenueInfo toVenueInfo(Venue venue);

    @Mapping(target = "eventSectorId", source = "id")
    @Mapping(target = "sectorId", source = "sector.id")
    @Mapping(target = "name", source = "sector.name")
    @Mapping(target = "standing", source = "sector.isStanding")
    @Mapping(target = "totalCapacity", source = "capacitySnapshot")
    @Mapping(target = "rowsCount", source = "sector.rowsCount")
    @Mapping(target = "seatsPerRow", source = "sector.seatsPerRow")
    @Mapping(target = "positionX", source = "sector.positionX")
    @Mapping(target = "positionY", source = "sector.positionY")
    @Mapping(target = "width", source = "sector.width")
    @Mapping(target = "height", source = "sector.height")
    @Mapping(target = "color", source = "sector.color")
    @Mapping(target = "prices", source = "ticketTypes")
    EventSectorInfo toSectorInfo(EventSector eventSector);

    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "ticketTypeName", source = "ticketType.name")
    @Mapping(target = "priceGross", expression = "java(estt.getPriceGrossCalculated())")
    TicketPriceInfo toTicketPriceInfo(EventSectorTicketType estt);

    @Mapping(target = "seatId", source = "seat.id")
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(target = "occupied", source = "isOccupied")
    SeatInfo toSeatInfo(Seat seat, boolean isOccupied);

    List<EventSectorInfo> toSectorInfoList(List<EventSector> sectors);

    default RowInfo toRowInfo(Integer rowNumber, List<SeatInfo> seats) {
        return new RowInfo(rowNumber, seats);
    }
}
