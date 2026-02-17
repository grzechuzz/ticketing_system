package pl.eticket.app.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SATSolverTest {

    private SATSolver solver;

    @BeforeEach
    void setUp() {
        solver = new SATSolver();
    }

    @Test
    @DisplayName("empty clause list is trivially satisfiable")
    void emptyClausesSatisfiable() {
        Map<Integer, Boolean> assignment = new HashMap<>();
        assertTrue(solver.solve(List.of(), assignment));
    }

    @Test
    @DisplayName("single positive clause: (x1) - satisfiable with x1=true")
    void singlePositiveClause() {
        Map<Integer, Boolean> assignment = new HashMap<>();
        boolean result = solver.solve(List.of(new Clause(1)), assignment);
        assertTrue(result);
        assertTrue(assignment.get(1));
    }

    @Test
    @DisplayName("single negative clause: (-x1) - satisfiable with x1=false")
    void singleNegativeClause() {
        Map<Integer, Boolean> assignment = new HashMap<>();
        boolean result = solver.solve(List.of(new Clause(-1)), assignment);
        assertTrue(result);
        assertFalse(assignment.get(1));
    }

    @Test
    @DisplayName("contradictory clauses: (x1) AND (-x1) - unsatisfiable")
    void contradictoryUnsatisfiable() {
        List<Clause> clauses = List.of(new Clause(1), new Clause(-1));
        Map<Integer, Boolean> assignment = new HashMap<>();
        assertFalse(solver.solve(clauses, assignment));
    }

    @Test
    @DisplayName("satisfiable with pre-assigned variable")
    void preAssigned() {
        // clause: (x1 OR x2), pre-assign x1=true
        Map<Integer, Boolean> assignment = new HashMap<>();
        assignment.put(1, true);
        assertTrue(solver.solve(List.of(new Clause(1, 2)), assignment));
        assertTrue(assignment.get(1));
    }

    @Test
    @DisplayName("pre-assignment making problem unsatisfiable")
    void preAssignedUnsatisfiable() {
        // clauses: (x1) AND (-x1 OR -x2), pre-assign x1=false
        Map<Integer, Boolean> assignment = new HashMap<>();
        assignment.put(1, false);
        assertFalse(solver.solve(List.of(new Clause(1)), assignment));
    }

    @Test
    @DisplayName("multiple clauses requiring specific assignment")
    void multipleClausesSpecificAssignment() {
        // (x1) AND (x2) AND (-x3)
        List<Clause> clauses = List.of(
                new Clause(1),
                new Clause(2),
                new Clause(-3)
        );
        Map<Integer, Boolean> assignment = new HashMap<>();
        assertTrue(solver.solve(clauses, assignment));
        assertTrue(assignment.get(1));
        assertTrue(assignment.get(2));
        assertFalse(assignment.get(3));
    }

    @Test
    @DisplayName("seat gap constraint: (-left OR middle OR -right) satisfied when no gap")
    void seatGapNoGap() {
        // All three seats occupied: left=T, middle=T, right=T
        // clause: (-1 OR 2 OR -3) -> -T OR T OR -T -> F OR T OR F -> TRUE
        Map<Integer, Boolean> assignment = new HashMap<>();
        assignment.put(1, true);
        assignment.put(2, true);
        assignment.put(3, true);
        assertTrue(solver.solve(List.of(new Clause(-1, 2, -3)), assignment));
    }

    @Test
    @DisplayName("seat gap constraint: (-left OR middle OR -right) fails when gap exists")
    void seatGapWithGap() {
        // left=T, middle=F (gap!), right=T
        // clause: (-1 OR 2 OR -3) -> -T OR F OR -T → F OR F OR F -> FALSE
        Map<Integer, Boolean> assignment = new HashMap<>();
        assignment.put(1, true);
        assignment.put(2, false);
        assignment.put(3, true);
        assertFalse(solver.solve(List.of(new Clause(-1, 2, -3)), assignment));
    }

    @Test
    @DisplayName("complex: 3 gap constraints for 5-seat row, all satisfiable")
    void fiveSeatRowNoGaps() {
        // Seats 1-5, all occupied. Constraints for seats 2,3,4:
        // (-1 OR 2 OR -3), (-2 OR 3 OR -4), (-3 OR 4 OR -5)
        List<Clause> clauses = List.of(
                new Clause(-1, 2, -3),
                new Clause(-2, 3, -4),
                new Clause(-3, 4, -5)
        );
        Map<Integer, Boolean> assignment = new HashMap<>();
        for (int i = 1; i <= 5; i++) assignment.put(i, true);

        assertTrue(solver.solve(clauses, assignment));
    }

    @Test
    @DisplayName("complex: gap in middle of 5-seat row is unsatisfiable")
    void fiveSeatRowWithGapInMiddle() {
        // Seats: T F T F T — gaps at 2 and 4
        // Constraint for seat 2: (-1 OR 2 OR -3) -> F OR F OR F -> FALSE
        List<Clause> clauses = List.of(
                new Clause(-1, 2, -3),
                new Clause(-2, 3, -4),
                new Clause(-3, 4, -5)
        );
        Map<Integer, Boolean> assignment = new HashMap<>();
        assignment.put(1, true);
        assignment.put(2, false);
        assignment.put(3, true);
        assignment.put(4, false);
        assignment.put(5, true);

        assertFalse(solver.solve(clauses, assignment));
    }

    @Test
    @DisplayName("solver finds assignment when variables are free")
    void solverFindsAssignment() {
        // (-1 OR 2 OR -3): solver should find some satisfying assignment
        Map<Integer, Boolean> assignment = new HashMap<>();
        assertTrue(solver.solve(List.of(new Clause(-1, 2, -3)), assignment));
        Clause c = new Clause(-1, 2, -3);
        assertTrue(c.isSatisfied(assignment));
    }
}
