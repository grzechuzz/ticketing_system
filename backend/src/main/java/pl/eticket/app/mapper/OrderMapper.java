package pl.eticket.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.eticket.app.dto.order.OrderItemResponse;
import pl.eticket.app.dto.order.OrderResponse;
import pl.eticket.app.entity.Order;
import pl.eticket.app.entity.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventName", source = "eventNameSnapshot")
    @Mapping(target = "eventDate", source = "eventDateSnapshot")
    @Mapping(target = "sectorName", source = "sectorNameSnapshot")
    @Mapping(target = "ticketTypeName", source = "ticketTypeNameSnapshot")
    @Mapping(target = "row", source = "rowSnapshot")
    @Mapping(target = "seatNumber", source = "seatNumberSnapshot")
    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponses(List<OrderItem> items);
}
