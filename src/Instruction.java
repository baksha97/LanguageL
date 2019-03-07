import java.util.Arrays;
import java.util.Map;

class Instruction {

    private String varName;
    private ModifyCommandType type;
    private String newStateName;

    public Instruction(String line){
        String[] parts = line.split(" ");
        if(parts[0].toLowerCase().equals("if")){
            configureIf(parts);
        }else{
            configureModify(parts);
        }
    }

    public void configureModify(String[] parts){
        varName = parts[0];
        if(parts[2].equals(varName) && parts[3].equals("+") && parts[4].equals("1")){
            type = ModifyCommandType.INCREMENT;
        }else if(parts[2].equals(varName) && parts[3].equals("-") && parts[4].equals("1")){
            type = ModifyCommandType.DECREMENT;
        }else{
            throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts));
        }
    }

    public void configureIf(String[] parts){
        varName = parts[1];
        type = ModifyCommandType.CONDITIONAL;
        newStateName = parts[5];
    }

    public String getVarName() {
        return varName;
    }

    public ModifyCommandType getType() {
        return type;
    }

    public StateBlock executeOn(Map<String, Variable> vars, Map<String, StateBlock> blockMap){
        switch (type){
            case INCREMENT:
                vars.get(varName).increment();
                break;
            case DECREMENT:
                vars.get(varName).decrement();
                break;
            case CONDITIONAL:
                if(vars.get(varName).notEqualsZero()){
                    return blockMap.get(newStateName);
                }
                break;
            default:
                throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);
        }
        return null;
    }

    @Override
    public String toString() {
        return getType() + ": " + getVarName() + "Next State: " + newStateName;
    }
}
