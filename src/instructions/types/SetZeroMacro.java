package instructions.types;

import instructions.Instructable;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class SetZeroMacro implements Instructable {

    private final String varName;
    private final InstructionType type;
    private String line;
    private int lineNumber;
    public SetZeroMacro(String line, String[] parts, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
        varName = parts[0];
        type = InstructionType.SET_ZERO_MACRO;
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
        return null;
    }

    @Override
    public List<Instructable> executeOn(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        if (type == InstructionType.SET_ZERO_MACRO) {
            vars.put(varName, 0);
        } else throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);

        return null;
    }

    @Override
    public String toString() {
        return "SetZeroMacro{" +
                "varName='" + varName + '\'' +
                ", type=" + type +
                '}';
    }
}
