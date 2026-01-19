package pl.eticket.app.dto.order;

import java.util.List;

public record SeatValidationResult(
    boolean valid,
    String message,
    List<SeatSuggestion> suggestions
) {
    public static SeatValidationResult ok() {
        return new SeatValidationResult(true, "Selection is valid", List.of());
    }

    public static SeatValidationResult invalid(String message, List<SeatSuggestion> suggestions) {
        return new SeatValidationResult(false, message, suggestions);
    }
}
