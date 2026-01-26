package pl.eticket.app.validation;

import java.util.*;

// Based on https://github.com/Software-Archetypes/archetypes/tree/main/configurator/src/main/java/softwarearchetypes/sat
public class Clause {

    private final List<Integer> literals;

    public Clause(Integer... literals) {
        this(Arrays.asList(literals));
    }

    public Clause(List<Integer> literals) {
        if (literals == null || literals.isEmpty()) {
            throw new IllegalArgumentException("Clause cannot be empty");
        }
        for (Integer literal : literals) {
            if (literal == 0) {
                throw new IllegalArgumentException("Literal cannot be 0");
            }
        }
        this.literals = new ArrayList<>(literals);
    }

    public List<Integer> literals() {
        return Collections.unmodifiableList(literals);
    }

    public boolean isSatisfied(Map<Integer, Boolean> assignment) {
        for (Integer literal : literals) {
            int var = Math.abs(literal);
            if (assignment.containsKey(var)) {
                boolean value = assignment.get(var);
                boolean expected = literal > 0;
                
                if (value == expected) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUnsatisfiable(Map<Integer, Boolean> assignment) {
        for (Integer literal : literals) {
            int var = Math.abs(literal);
            
            if (!assignment.containsKey(var)) {
                return false;
            }
            
            boolean value = assignment.get(var);
            boolean expected = literal > 0;
            
            if (value == expected) {
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
