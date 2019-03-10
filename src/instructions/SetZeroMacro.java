package instructions;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class SetZeroMacro implements Instructable {

    @Override
    public String getVarName() {
        return varName;
    }

    private final String varName;
    private final InstructionType type;

    public SetZeroMacro(String[] parts) {
        varName = parts[0];
        type = InstructionType.SET_ZERO_MACRO;
    }

    @Override
    public InstructionType getType() {
        return type;
    }

    @Override
    public boolean willChangeState(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        return false;
    }

    @Override
    public List<Instructable> executeOn(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        if (type == InstructionType.SET_ZERO_MACRO) {
            vars.put(varName, 0);
        } else throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);

        return null;
    }

    @Override
    public List<Instructable> getGoToMacroNewState(LinkedMap<String, List<Instructable>> states) {
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
