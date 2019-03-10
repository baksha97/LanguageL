package instructions;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class GoToMacro implements Instructable {

    private final InstructionType type;

    public String getNewStateName() {
        return newStateName;
    }

    private final String newStateName;

    public GoToMacro(String[] parts){

        type = InstructionType.GOTO_MACRO;
        newStateName = parts[1];
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
        return null;
    }

    @Override
    public List<Instructable> getGoToMacroNewState(LinkedMap<String, List<Instructable>> states) {
        return states.get(newStateName);
    }

    @Override
    public String getVarName() {
        return null;
    }

    @Override
    public String toString() {
        return "GoToMacro{" +
                "type=" + type +
                ", newStateName='" + newStateName + '\'' +
                '}';
    }
}
