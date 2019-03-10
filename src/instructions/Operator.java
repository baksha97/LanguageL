package instructions;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public class Operator implements Instructable {

    @Override
    public String getVarName() {
        return varName;
    }

    private final String varName;
    private final InstructionType type;

    public Operator(String varName, InstructionType type){
        this.varName = varName;
        this.type = type;
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
        switch (type){
            case INCREMENT:
                vars.put(varName, vars.getOrDefault(varName, 0) + 1);
                break;
            case DECREMENT:
                vars.put(varName, vars.getOrDefault(varName, 0) - 1);
                break;
            default:
                throw new IllegalArgumentException("Cannot create Operator Instruction for" + type + " on " + varName);
        }
        return null;
    }

    @Override
    public List<Instructable> getGoToMacroNewState(LinkedMap<String, List<Instructable>> states) {
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
