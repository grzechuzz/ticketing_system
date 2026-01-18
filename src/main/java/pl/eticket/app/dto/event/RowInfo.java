package pl.eticket.app.dto.event;

import java.util.List;

public record RowInfo(
    Integer rowNumber,
    List<SeatInfo> seats
) { }
