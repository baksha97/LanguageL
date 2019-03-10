package instructions;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class Conditional implements Instructable {

    private final boolean stateChangeable;

    @Override
    public String getVarName() {
        return varName;
    }

    private final String varName;
    private final InstructionType type;
    private final String newStateName;

    public Conditional(String[] parts){

        stateChangeable = true;
        varName = parts[1];
        type = InstructionType.CONDITIONAL;
        newStateName = parts[5];
    }

    @Override
    public InstructionType getType() {
        return type;
    }

    @Override
    public boolean willChangeState(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
        return vars.get(varName) != 0 && states.containsKey(newStateName);
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
    public List<Instructable> getGoToMacroNewState(LinkedMap<String, List<Instructable>> states) {
        return null;
    }

    @Override
    public String toString() {
        return "Conditional{" +
                "stateChangeable=" + stateChangeable +
                ", varName='" + varName + '\'' +
                ", type=" + type +
                ", newStateName='" + newStateName + '\'' +
                '}';
    }
}
