package instructions.types;

import instructions.Instructable;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class Operator implements Instructable {

    private final String varName;
    private final InstructionType type;
    private final String line;
    private int lineNumber;

    public Operator(String line, String varName, InstructionType type, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.varName = varName;
        this.type = type;
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
        switch (type) {
            case INCREMENT:
                vars.put(varName, vars.getOrDefault(varName, 0) + 1);
                break;
            case DECREMENT:
                vars.put(varName, vars.getOrDefault(varName, 0) - 1);
                if(vars.get(varName) < 0){
                    vars.put(varName, 0);
                }
                break;
            default:
                throw new IllegalArgumentException("Cannot create Operator Instruction for" + type + " on " + varName);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "varName='" + varName + '\'' +
                ", type=" + type +
                '}';
    }
}
