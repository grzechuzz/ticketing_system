package pl.eticket.app.validation;

import java.util.*;

public class SATSolver {

    public boolean solve(List<Clause> clauses, Map<Integer, Boolean> assignment) {
        if (clauses.isEmpty()) {
            return true;
        }

        return sat(clauses, new HashMap<>(assignment), assignment);
    }

    private boolean sat(List<Clause> clauses, Map<Integer, Boolean> current, Map<Integer, Boolean> result) {
        if (allSatisfied(clauses, current)) {
            result.clear();
            result.putAll(current);
            return true;
        }

        if (anyUnsatisfiable(clauses, current)) {
            return false;
        }

        Integer unassigned = findUnassignedVariable(clauses, current);
        if (unassigned == null) {
            return false;
        }

        int variable = Math.abs(unassigned);

        Map<Integer, Boolean> withTrue = new HashMap<>(current);
        withTrue.put(variable, true);
        if (sat(clauses, withTrue, result)) {
            return true;
        }

        Map<Integer, Boolean> withFalse = new HashMap<>(current);
        withFalse.put(variable, false);
        return sat(clauses, withFalse, result);
    }

    private boolean allSatisfied(List<Clause> clauses, Map<Integer, Boolean> assignment) {
        return clauses.stream().allMatch(c -> c.isSatisfied(assignment));
    }

    private boolean anyUnsatisfiable(List<Clause> clauses, Map<Integer, Boolean> assignment) {
        return clauses.stream().anyMatch(c -> c.isUnsatisfiable(assignment));
    }

    private Integer findUnassignedVariable(List<Clause> clauses, Map<Integer, Boolean> assignment) {
        for (Clause clause : clauses) {
            for (Integer literal : clause.literals()) {
                int var = Math.abs(literal);
                if (!assignment.containsKey(var)) {
                    return literal;
                }
            }
        }
        return null;
    }

    public boolean isSatisfiableWithPartialAssignment(List<Clause> clauses, Map<Integer, Boolean> partial) {
        Map<Integer, Boolean> result = new HashMap<>();
        return solve(clauses, new HashMap<>(partial));
    }
}
