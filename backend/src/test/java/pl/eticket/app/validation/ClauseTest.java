package pl.eticket.app.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClauseTest {

    @Test
    @DisplayName("should create clause from varargs")
    void createFromVarargs() {
        Clause clause = new Clause(1, -2, 3);
        assertEquals(List.of(1, -2, 3), clause.literals());
    }

    @Test
    @DisplayName("should create clause from list")
    void createFromList() {
        Clause clause = new Clause(List.of(4, -5));
        assertEquals(List.of(4, -5), clause.literals());
    }

    @Test
    @DisplayName("should reject empty clause")
    void rejectEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Clause(List.of()));
    }

    @Test
    @DisplayName("should reject clause containing literal 0")
    void rejectZeroLiteral() {
        assertThrows(IllegalArgumentException.class, () -> new Clause(1, 0, -2));
    }

    @Test
    @DisplayName("should return unmodifiable literals list")
    void literalsUnmodifiable() {
        Clause clause = new Clause(1, 2);
        assertThrows(UnsupportedOperationException.class, () -> clause.literals().add(3));
    }

    @Test
    @DisplayName("satisfied when positive literal is true")
    void satisfiedPositiveTrue() {
        Clause clause = new Clause(1, 2, 3);
        Map<Integer, Boolean> assignment = Map.of(1, true);
        assertTrue(clause.isSatisfied(assignment));
    }

    @Test
    @DisplayName("satisfied when negative literal is false")
    void satisfiedNegativeFalse() {
        // clause: (-1), meaning "not x1" - satisfied when x1=false
        Clause clause = new Clause(-1);
        Map<Integer, Boolean> assignment = Map.of(1, false);
        assertTrue(clause.isSatisfied(assignment));
    }

    @Test
    @DisplayName("not satisfied when all literals contradict assignment")
    void notSatisfied() {
        // clause: (x1 OR x2), assignment: x1=false, x2=false
        Clause clause = new Clause(1, 2);
        Map<Integer, Boolean> assignment = Map.of(1, false, 2, false);
        assertFalse(clause.isSatisfied(assignment));
    }

    @Test
    @DisplayName("not satisfied when no variables assigned yet")
    void notSatisfiedNoAssignment() {
        Clause clause = new Clause(1, 2);
        assertFalse(clause.isSatisfied(Map.of()));
    }

    @Test
    @DisplayName("satisfied if at least one literal matches in mixed clause")
    void satisfiedMixed() {
        // clause: (-1 OR 2 OR -3) - satisfied when x2=true
        Clause clause = new Clause(-1, 2, -3);
        Map<Integer, Boolean> assignment = Map.of(1, true, 2, true, 3, true);
        assertTrue(clause.isSatisfied(assignment));
    }

    @Test
    @DisplayName("unsatisfiable when all literals assigned and none matches")
    void unsatisfiable() {
        // clause: (x1 OR x2), assignment: x1=false, x2=false
        Clause clause = new Clause(1, 2);
        assertTrue(clause.isUnsatisfiable(Map.of(1, false, 2, false)));
    }

    @Test
    @DisplayName("not unsatisfiable when unassigned variable exists")
    void notUnsatisfiableWithUnassigned() {
        Clause clause = new Clause(1, 2);
        // only x1 assigned=false, x2 still free so could still be satisfied
        assertFalse(clause.isUnsatisfiable(Map.of(1, false)));
    }

    @Test
    @DisplayName("not unsatisfiable when at least one literal matches")
    void notUnsatisfiableWhenSatisfied() {
        Clause clause = new Clause(1, -2);
        assertFalse(clause.isUnsatisfiable(Map.of(1, true, 2, true)));
    }

    @Test
    @DisplayName("single negative literal: unsatisfiable when variable is true")
    void singleNegativeUnsatisfiable() {
        Clause clause = new Clause(-5);
        assertTrue(clause.isUnsatisfiable(Map.of(5, true)));
    }

    @Test
    @DisplayName("single negative literal: satisfiable when variable is false")
    void singleNegativeSatisfiable() {
        Clause clause = new Clause(-5);
        assertFalse(clause.isUnsatisfiable(Map.of(5, false)));
    }
}
