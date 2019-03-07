import java.util.List;
import java.util.Map;

class StateBlock {

    private String name;
    private List<Instruction> instructions;
    private int pos;
    public StateBlock(String name, List<Instruction> instructions){
        this.name = name;
        this.instructions = instructions;
        this.pos = 0;
    }

    public String getName() {
        return name;
    }

    public boolean hasNextInstruction(){
        return pos < instructions.size();
    }

    public StateBlock executeNext(Map<String, Variable> varMap, Map<String, StateBlock> blockMap) {
        return instructions.get(pos++).executeOn(varMap, blockMap);
    }

    @Override
    public String toString() {
        return "State: " + getName() + "\n\tInstructions: " + instructions;
    }
}
