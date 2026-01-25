package pl.eticket.app.dto.order;

import java.util.List;

public record SeatSuggestion(
    int row,
    List<Integer> seatNumbers
) { }
