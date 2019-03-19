package instructions.types;

import instructions.Instructable;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class Conditional implements Instructable {

    private final String line;
    private final String varName;
    private final InstructionType type;
    private final String newStateName;
    private int lineNumber;
    public Conditional(String line, String[] parts, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
        varName = parts[1];
        type = InstructionType.CONDITIONAL;
        newStateName = parts[5];
    }

    @Override
    public String getVarName() {
        return varName;
    }

    @Override
    public String originalLine() {
        return line;
    }

    @Override
    public int lineNumber() {
        return lineNumber;
    }

    @Override
    public InstructionType getType() {
        return type;
    }

    @Override
    public String nextState(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        if (vars.get(varName) != 0 && states.containsKey(newStateName)) {
            return newStateName;
        }
        return null;
    }

    @Override
    public List<Instructable> executeOn(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        if (type == InstructionType.CONDITIONAL) {
            if (vars.get(varName) != 0)
                return states.get(newStateName);
            else
                return null;

        } else throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);

    }

    @Override
    public String toString() {
        return "Conditional{" +
                "varName='" + varName + '\'' +
                ", type=" + type +
                ", newStateName='" + newStateName + '\'' +
                '}';
    }
}
