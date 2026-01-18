package pl.eticket.app.dto.event;

public record SeatInfo(
    Long seatId,
    Integer seatNumber,
    boolean occupied
) { }
