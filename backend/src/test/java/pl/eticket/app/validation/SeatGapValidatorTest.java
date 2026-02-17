package pl.eticket.app.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import pl.eticket.app.TestDataFactory;
import pl.eticket.app.dto.order.SeatValidationResult;
import pl.eticket.app.entity.Seat;
import pl.eticket.app.entity.Sector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SeatGapValidatorTest {

    private SeatGapValidator validator;
    private Sector sector;
    private List<Seat> allSeats;

    @BeforeEach
    void setUp() {
        validator = new SeatGapValidator();
        sector = TestDataFactory.seatedSector(1L, 5, 10);
        allSeats = TestDataFactory.generateSeats(sector, 5, 10); // 5 rows x 10 seats = 50 seats
    }

    private Long seatId(int row, int seatNum) {
        return (long) ((row - 1) * 10 + seatNum);
    }

    @Nested
    @DisplayName("Valid selections")
    class ValidSelections {

        @Test
        @DisplayName("empty selection is always valid")
        void emptySelection() {
            SeatValidationResult result = validator.validate(allSeats, Set.of(), Set.of());
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("single seat in empty row is valid")
        void singleSeatEmptyRow() {
            SeatValidationResult result = validator.validate(allSeats, Set.of(), Set.of(seatId(1, 5)));
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("two adjacent seats in empty row are valid")
        void twoAdjacentSeats() {
            Set<Long> selected = Set.of(seatId(1, 4), seatId(1, 5));
            SeatValidationResult result = validator.validate(allSeats, Set.of(), selected);
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("three consecutive seats are valid")
        void threeConsecutive() {
            Set<Long> selected = Set.of(seatId(2, 3), seatId(2, 4), seatId(2, 5));
            SeatValidationResult result = validator.validate(allSeats, Set.of(), selected);
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("selecting seat next to already occupied seat - no gap")
        void adjacentToOccupied() {
            // Seats 1-3 occupied, user selects seat 4 — no gap
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2), seatId(1, 3));
            Set<Long> selected = Set.of(seatId(1, 4));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("selecting first seat in row is valid")
        void firstSeat() {
            SeatValidationResult result = validator.validate(allSeats, Set.of(), Set.of(seatId(1, 1)));
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("selecting last seat in row is valid")
        void lastSeat() {
            SeatValidationResult result = validator.validate(allSeats, Set.of(), Set.of(seatId(1, 10)));
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("leaving gap of 2+ empty seats is valid (not a single-seat gap)")
        void gapOfTwo() {
            // Occupy seats 1-2, select seat 5 → seats 3,4 are free (gap of 2, OK)
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(1, 5));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("full row is valid")
        void fullRow() {
            Set<Long> selected = new HashSet<>();
            for (int i = 1; i <= 10; i++) selected.add(seatId(1, i));
            SeatValidationResult result = validator.validate(allSeats, Set.of(), selected);
            assertTrue(result.valid());
        }
    }

    @Nested
    @DisplayName("Invalid selections - single-seat gaps")
    class InvalidSelections {

        @Test
        @DisplayName("creating gap between two occupied groups")
        void gapBetweenGroups() {
            // Occupied: 1,2. Selected: 4,5 -> seat 3 is a single-seat gap
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(1, 4), seatId(1, 5));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertFalse(result.valid());
            assertNotNull(result.message());
        }

        @Test
        @DisplayName("creating gap at one side of selection")
        void gapOnOneSide() {
            // Occupied: seat 1. User selects seat 3 -> seat 2 is a single-seat gap
            Set<Long> occupied = Set.of(seatId(1, 1));
            Set<Long> selected = Set.of(seatId(1, 3));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertFalse(result.valid());
        }

        @Test
        @DisplayName("selecting seats that sandwich a gap")
        void sandwichGap() {
            // Row 1: select seats 3 and 5 -> seat 4 is gap between them
            Set<Long> selected = Set.of(seatId(1, 3), seatId(1, 5));
            SeatValidationResult result = validator.validate(allSeats, Set.of(), selected);
            assertFalse(result.valid());
        }

        @Test
        @DisplayName("gap near end of row: occupied=[8,9,10], selected=[6] -> seat 7 is gap")
        void gapNearEnd() {
            Set<Long> occupied = Set.of(seatId(1, 8), seatId(1, 9), seatId(1, 10));
            Set<Long> selected = Set.of(seatId(1, 6));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertFalse(result.valid());
        }
    }

    @Nested
    @DisplayName("Suggestions")
    class Suggestions {

        @Test
        @DisplayName("provides alternative suggestions when selection is invalid")
        void suggestionsProvided() {
            // Occupied: seats 1,2. Selected: seat 4 -> gap at 3
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(1, 4));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertFalse(result.valid());
            assertFalse(result.suggestions().isEmpty(), "Should provide suggestions");
        }

        @Test
        @DisplayName("suggestions contain valid seat numbers")
        void suggestionsAreValid() {
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(1, 4));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);

            result.suggestions().forEach(suggestion -> {
                assertTrue(suggestion.row() >= 1 && suggestion.row() <= 5);
                suggestion.seatNumbers().forEach(sn ->
                        assertTrue(sn >= 1 && sn <= 10, "Seat number should be within range"));
            });
        }

        @Test
        @DisplayName("suggestion seat 3 (closing the gap) should be among alternatives")
        void closingGapSuggested() {
            // Occupied: 1,2. Selected: 4. Gap at 3. Seat 3 should be suggested.
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(1, 4));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);

            boolean hasSeat3Suggestion = result.suggestions().stream()
                    .anyMatch(s -> s.row() == 1 && s.seatNumbers().contains(3));
            assertTrue(hasSeat3Suggestion, "Should suggest seat 3 to close the gap");
        }

        @Test
        @DisplayName("suggestions have correct number of seats matching selection count")
        void suggestionsMatchCount() {
            // Select 2 seats that create a gap
            Set<Long> occupied = Set.of(seatId(1, 1));
            Set<Long> selected = Set.of(seatId(1, 3), seatId(1, 4));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);

            if (!result.valid() && !result.suggestions().isEmpty()) {
                result.suggestions().forEach(s ->
                        assertEquals(2, s.seatNumbers().size(),
                                "Suggestion should have same number of seats as selection"));
            }
        }
    }

    @Nested
    @DisplayName("Multi-row scenarios")
    class MultiRow {

        @Test
        @DisplayName("gap in row 1 does not affect row 2 validity")
        void independentRows() {
            // Row 1: occupied 1,2 - Row 2: select seat 5 (no issue in row 2)
            Set<Long> occupied = Set.of(seatId(1, 1), seatId(1, 2));
            Set<Long> selected = Set.of(seatId(2, 5));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("gap created in same row as selection is detected")
        void gapInSameRow() {
            // Row 3: occupied seat 1, select seat 3 -> gap at 2
            Set<Long> occupied = Set.of(seatId(3, 1));
            Set<Long> selected = Set.of(seatId(3, 3));
            SeatValidationResult result = validator.validate(allSeats, occupied, selected);
            assertFalse(result.valid());
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("2-seat sector: selecting 1 of 2 is valid")
        void twoSeatSector() {
            Sector smallSector = TestDataFactory.seatedSector(99L, 1, 2);
            List<Seat> smallSeats = TestDataFactory.generateSeats(smallSector, 1, 2);
            SeatValidationResult result = validator.validate(smallSeats, Set.of(), Set.of(1L));
            assertTrue(result.valid());
        }

        @Test
        @DisplayName("3-seat sector: middle seat gap detected")
        void threeSeatMiddleGap() {
            Sector smallSector = TestDataFactory.seatedSector(98L, 1, 3);
            List<Seat> smallSeats = TestDataFactory.generateSeats(smallSector, 1, 3);
            // Select seats 1 and 3 → gap at 2
            SeatValidationResult result = validator.validate(smallSeats, Set.of(), Set.of(1L, 3L));
            assertFalse(result.valid());
        }

        @Test
        @DisplayName("all seats occupied except one in middle — selecting nothing is valid")
        void noSelectionAlwaysValid() {
            Set<Long> occupied = new HashSet<>();
            for (int i = 1; i <= 10; i++) {
                if (i != 5) occupied.add(seatId(1, i));
            }
            SeatValidationResult result = validator.validate(allSeats, occupied, Set.of());
            assertTrue(result.valid());
        }
    }
}
