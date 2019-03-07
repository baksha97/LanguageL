import org.apache.commons.collections4.map.LinkedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Run {

    public static void main(String ... args) throws FileNotFoundException {
//        File file = new File("pro.txt");
        File file = new File("p2.txt");
        Scanner in = new Scanner(file);

        LinkedMap<String, List<Instruction>> states = new LinkedMap<>();
        HashMap<String, Variable> vals = new HashMap<>();

        //initialize blocks
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.isEmpty()) continue;
            if(line.contains("[")){
                states.put(line.replace("[","").replace("]",""), new ArrayList<>());
            }else{
                Instruction instruction = new Instruction(line);
                states.get(states.lastKey()).add(instruction);
                if(instruction.getType() == Command.GOTO_MACRO) continue;
                vals.put(instruction.getVarName(), new Variable(instruction.getVarName()));
            }
        }


        //execute blocks
        System.out.println("init... \n");
        vals.put("X", new Variable("X", 3));
        System.out.println(states);
        System.out.println(vals);

        String currentState = states.firstKey();
        List<Instruction> instructions = states.get(currentState);

        System.out.println("\nExecuting...");
        while(instructions != null){
            for(int i=0; i<instructions.size(); i++){
                Instruction inst = instructions.get(i);

                if(inst.isGoToMacro()){
                    instructions = inst.getNewGoToMacro(states);
                    break;
                }

                if(inst.isStateChangeable() && inst.willChangeState(vals)){
                    instructions = inst.executeOn(states, vals);
                    break;
                }else{
                    inst.executeOn(states, vals);
                }
                System.out.println(vals);
            }

        }
    }

    private static class Instruction {

        private String varName;
        private Command type;
        private String newStateName;
        private String variableNameToCopy;

        private boolean stateChangeable;

        public Instruction(String line){
            String[] parts = line.split(" ");
            if(parts[0].equalsIgnoreCase("if")){
                configureIf(parts);
            }else if(parts[0].equalsIgnoreCase("GOTO")){
                type = Command.GOTO_MACRO;
                newStateName = parts[1];
            }else{
                configureModify(parts);
            }
        }

        //[Z, <-, Z, +, 1]
        public void configureModify(String[] parts){
            stateChangeable = false;
            varName = parts[0];
            if(parts[2].equals("0")){
                type = Command.SET_ZERO_MACRO;
            }
            else if(!parts[2].equals(varName) && parts.length == 3){
                type = Command.COPY_MACRO;
                variableNameToCopy = parts[2];
            }else if(parts[2].equals(varName) && parts[3].equals("+") && parts[4].equals("1")){
                type = Command.INCREMENT;
            }else if(parts[2].equals(varName) && parts[3].equals("-") && parts[4].equals("1")){
                type = Command.DECREMENT;
            }else{
                throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts));
            }
        }

        public void configureIf(String[] parts){
            stateChangeable = true;
            varName = parts[1];
            type = Command.CONDITIONAL;
            newStateName = parts[5];
        }

        public String getVarName() {
            return varName;
        }

        public Command getType() {
            return type;
        }

        public boolean isStateChangeable() {
            return stateChangeable;
        }

        public boolean willChangeState(Map<String, Variable> vars){
            return vars.get(varName).notEqualsZero();
        }

        public boolean isGoToMacro(){
            return type == Command.GOTO_MACRO;
        }

        public List<Instruction> executeOn(Map<String, List<Instruction>> states, Map<String, Variable> vars){
            switch (type){
                case INCREMENT:
                    vars.get(varName).increment();
                    break;
                case DECREMENT:
                    vars.get(varName).decrement();
                    break;
                case SET_ZERO_MACRO:
                    vars.get(varName).setZero();
                    break;
                case COPY_MACRO:
                    vars.get(varName).copyContents(vars.get(variableNameToCopy));
                    break;
                case CONDITIONAL:
                    if(vars.get(varName).notEqualsZero()){
                        return states.get(newStateName);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Cannot modify " + varName + " with " + type);
            }
            return null;
        }


        @Override
        public String toString() {
            return getType() + ": " + getVarName() + " -> Next State: " + newStateName;
        }

        public List<Instruction> getNewGoToMacro(LinkedMap<String, List<Instruction>> states) {
            return states.get(newStateName);
        }
    }
}
