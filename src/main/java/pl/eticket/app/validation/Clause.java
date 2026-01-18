package pl.eticket.app.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Clause {

    private final List<Integer> literals;

    public Clause(Integer... literals) {
        this.literals = Arrays.asList(literals);
    }

    public Clause(List<Integer> literals) {
        this.literals = new ArrayList<>(literals);
    }

    public List<Integer> literals() {
        return literals;
    }

    public boolean isSatisfied(Map<Integer, Boolean> assignment) {
        for (int literal : literals) {
            int var = Math.abs(literal);
            if (assignment.containsKey(var)) {
                boolean value = assignment.get(var);
                if (value == (literal > 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUnsatisfiable(Map<Integer, Boolean> assignment) {
        for (int literal : literals) {
            int var = Math.abs(literal);
            if (!assignment.containsKey(var)) {
                return false;
            }

            boolean value = assignment.get(var);
            if (value == (literal > 0)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return literals.toString();
    }
}