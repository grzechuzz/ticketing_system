package pl.eticket.app.dto.order;

public record AddSeatsResponse(
    OrderResponse order,
    SeatValidationResult validation
) {
    public static AddSeatsResponse success(OrderResponse order) {
        return new AddSeatsResponse(order, SeatValidationResult.ok());
    }

    public static AddSeatsResponse validationFailed(OrderResponse order, SeatValidationResult validation) {
        return new AddSeatsResponse(order, validation);
    }
}
