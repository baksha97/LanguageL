package instructions;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Map;

public interface Instructable {
    InstructionType getType();

    boolean willChangeState(Map<String, List<Instructable>> states, Map<String, Integer> vars);

    List<Instructable> executeOn(Map<String, List<Instructable>> states, Map<String, Integer> vars);

    List<Instructable> getGoToMacroNewState(LinkedMap<String, List<Instructable>> states);

    String getVarName();


}
