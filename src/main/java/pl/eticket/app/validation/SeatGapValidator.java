package pl.eticket.app.validation;

import org.springframework.stereotype.Component;
import pl.eticket.app.entity.Seat;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeatGapValidator {
    private final SATSolver solver = new SATSolver();

    public record ValidationResult(
            boolean valid,
            String message,
            List<SeatSuggestion> suggestions
    ) {
        public static ValidationResult ok() {
            return new ValidationResult(true, "Selection is valid", Collections.emptyList());
        }

        public static ValidationResult invalid(String message, List<SeatSuggestion> suggestions) {
            return new ValidationResult(false, message, suggestions);
        }
    }

    public record SeatSuggestion(
            int row,
            List<Integer> seatNumbers
    ) {}

    public ValidationResult validate(
            List<Seat> allSeatsInSector,
            Set<Long> occupiedSeatIds,
            Set<Long> selectedSeatIds
    ) {
        if (selectedSeatIds.isEmpty()) {
            return ValidationResult.ok();
        }

        Map<String, Long> coordToId = new HashMap<>();
        Map<Long, int[]> idToCoord = new HashMap<>();
        int maxRow = 0;
        int maxCol = 0;

        for (Seat seat : allSeatsInSector) {
            String key = seat.getRowNumber() + "," + seat.getSeatNumber();
            coordToId.put(key, seat.getId());
            idToCoord.put(seat.getId(), new int[]{seat.getRowNumber(), seat.getSeatNumber()});
            maxRow = Math.max(maxRow, seat.getRowNumber());
            maxCol = Math.max(maxCol, seat.getSeatNumber());
        }

        Set<Integer> rowsToCheck = new HashSet<>();
        for (Long seatId : selectedSeatIds) {
            int[] coord = idToCoord.get(seatId);
            if (coord != null) {
                int row = coord[0];
                rowsToCheck.add(row);
                if (row > 1) rowsToCheck.add(row - 1);
                if (row < maxRow) rowsToCheck.add(row + 1);
            }
        }

        Set<Long> occupiedAfterSelection = new HashSet<>(occupiedSeatIds);
        occupiedAfterSelection.addAll(selectedSeatIds);

        List<Clause> clauses = generateClauses(allSeatsInSector, rowsToCheck, maxCol);

        Map<Integer, Boolean> assignment = new HashMap<>();
        for (Seat seat : allSeatsInSector) {
            if (rowsToCheck.contains(seat.getRowNumber())) {
                int var = toVariable(seat.getRowNumber(), seat.getSeatNumber(), maxCol);
                assignment.put(var, occupiedAfterSelection.contains(seat.getId()));
            }
        }

        boolean satisfiable = solver.isSatisfiableWithPartialAssignment(clauses, assignment);

        if (satisfiable) {
            return ValidationResult.ok();
        }

        int numSelectedSeats = selectedSeatIds.size();
        List<SeatSuggestion> alternatives = findAlternatives(
                allSeatsInSector, occupiedSeatIds, rowsToCheck, numSelectedSeats, maxCol, coordToId
        );

        if (alternatives.isEmpty()) {
            return ValidationResult.ok();
        }

        return ValidationResult.invalid(
                "This selection creates a single-seat gap. Please choose adjacent seats.",
                alternatives
        );
    }

    private List<Clause> generateClauses(List<Seat> seats, Set<Integer> rowsToCheck, int maxCol) {
        List<Clause> clauses = new ArrayList<>();

        Map<Integer, List<Seat>> seatsByRow = seats.stream()
                .filter(s -> rowsToCheck.contains(s.getRowNumber()))
                .collect(Collectors.groupingBy(Seat::getRowNumber));

        for (Map.Entry<Integer, List<Seat>> entry : seatsByRow.entrySet()) {
            int row = entry.getKey();
            List<Seat> rowSeats = entry.getValue();
            rowSeats.sort(Comparator.comparingInt(Seat::getSeatNumber));

            for (int i = 1; i < rowSeats.size() - 1; i++) {
                int leftCol = rowSeats.get(i - 1).getSeatNumber();
                int middleCol = rowSeats.get(i).getSeatNumber();
                int rightCol = rowSeats.get(i + 1).getSeatNumber();

                if (rightCol - middleCol == 1 && middleCol - leftCol == 1) {
                    int leftVar = toVariable(row, leftCol, maxCol);
                    int middleVar = toVariable(row, middleCol, maxCol);
                    int rightVar = toVariable(row, rightCol, maxCol);

                    clauses.add(new Clause(-leftVar, middleVar, -rightVar));
                }
            }
        }

        return clauses;
    }

    private List<SeatSuggestion> findAlternatives(
            List<Seat> allSeats,
            Set<Long> occupied,
            Set<Integer> rowsToCheck,
            int numSeats,
            int maxCol,
            Map<String, Long> coordToId
    ) {
        List<SeatSuggestion> suggestions = new ArrayList<>();

        Map<Integer, List<Integer>> freeByRow = new HashMap<>();
        for (Seat seat : allSeats) {
            if (rowsToCheck.contains(seat.getRowNumber()) && !occupied.contains(seat.getId())) {
                freeByRow.computeIfAbsent(seat.getRowNumber(), k -> new ArrayList<>())
                        .add(seat.getSeatNumber());
            }
        }

        for (Map.Entry<Integer, List<Integer>> entry : freeByRow.entrySet()) {
            int row = entry.getKey();
            List<Integer> freeSeats = entry.getValue();
            Collections.sort(freeSeats);

            List<List<Integer>> sequences = findConsecutiveSequences(freeSeats, numSeats);

            for (List<Integer> seq : sequences) {
                Set<Long> hypotheticalSelected = seq.stream()
                        .map(col -> coordToId.get(row + "," + col))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                Set<Long> hypotheticalOccupied = new HashSet<>(occupied);
                hypotheticalOccupied.addAll(hypotheticalSelected);

                List<Clause> clauses = generateClauses(allSeats, Set.of(row), maxCol);
                Map<Integer, Boolean> assignment = new HashMap<>();
                for (Seat seat : allSeats) {
                    if (seat.getRowNumber() == row) {
                        int var = toVariable(row, seat.getSeatNumber(), maxCol);
                        assignment.put(var, hypotheticalOccupied.contains(seat.getId()));
                    }
                }

                if (solver.isSatisfiableWithPartialAssignment(clauses, assignment)) {
                    suggestions.add(new SeatSuggestion(row, seq));
                }
            }
        }

        return suggestions.stream().limit(5).collect(Collectors.toList());
    }

    private List<List<Integer>> findConsecutiveSequences(List<Integer> sorted, int length) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i <= sorted.size() - length; i++) {
            List<Integer> seq = new ArrayList<>();
            boolean consecutive = true;

            for (int j = 0; j < length; j++) {
                if (j > 0 && sorted.get(i + j) != sorted.get(i + j - 1) + 1) {
                    consecutive = false;
                    break;
                }
                seq.add(sorted.get(i + j));
            }

            if (consecutive && seq.size() == length) {
                result.add(seq);
            }
        }

        return result;
    }

    private int toVariable(int row, int col, int maxCol) {
        return row * (maxCol + 1) + col + 1;
    }
}
