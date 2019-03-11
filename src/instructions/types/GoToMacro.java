package instructions.types;

import instructions.Instructable;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class GoToMacro implements Instructable {

    private final InstructionType type;
    private final String newStateName;
    private final String line;
    public GoToMacro(String line, String[] parts) {
        this.line = line;
        type = InstructionType.GOTO_MACRO;
        newStateName = parts[1];
    }

    public String getNewStateName() {
        return newStateName;
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
    public String originalLine() {
        return line;
    }

    @Override
    public String toString() {
        return "GoToMacro{" +
                "type=" + type +
                ", newStateName='" + newStateName + '\'' +
                '}';
    }
}
