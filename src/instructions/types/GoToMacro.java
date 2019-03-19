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
    private int lineNumber;

    public GoToMacro(String line, String[] parts, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
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
        return newStateName;
    }

    @Override
    public List<Instructable> executeOn(Map<String, List<Instructable>> states, Map<String, Integer> vars) {
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
    public int lineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return "GoToMacro{" +
                "type=" + type +
                ", newStateName='" + newStateName + '\'' +
                '}';
    }
}
