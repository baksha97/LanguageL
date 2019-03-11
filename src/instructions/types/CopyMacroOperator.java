package instructions.types;

import instructions.Instructable;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class CopyMacroOperator implements Instructable {

    private final String varName;
    private final String variableNameToCopy;
    private final InstructionType type;
    private final String line;

    public CopyMacroOperator(String line, String[] parts) {
        this.line = line;
        varName = parts[0];
        type = InstructionType.COPY_MACRO;
        variableNameToCopy = parts[2];
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
        if (type == InstructionType.COPY_MACRO) {
            if (!vars.containsKey(variableNameToCopy))
                throw new IllegalArgumentException(variableNameToCopy + " does not exist to be copied to " + varName);
            vars.put(varName, vars.get(variableNameToCopy));
        } else throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);

        return null;
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
    public String toString() {
        return "CopyMacroOperator{" +
                "varName='" + varName + '\'' +
                ", variableNameToCopy='" + variableNameToCopy + '\'' +
                ", type=" + type +
                '}';
    }
}
