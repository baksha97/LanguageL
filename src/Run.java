import org.apache.commons.collections4.map.LinkedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Run {

    public static void main(String ... args) throws FileNotFoundException {
        File file = new File("pro.txt");
        Scanner in = new Scanner(file);



        LinkedMap<String, List<Instruction>> states = new LinkedMap<>();
        HashMap<String, Variable> vals = new HashMap<>();



        //initialize blocks
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.contains("[")){
                states.put(line.replace("[","").replace("]",""), new ArrayList<>());
//                System.out.println("Put in state: " +  line);
            }else{
                Instruction instruction = new Instruction(line);
                vals.put(instruction.getVarName(), new Variable(instruction.getVarName()));
                states.get(states.lastKey()).add(instruction);
            }
        }


        vals.put("X", new Variable("X", 3));
        System.out.println(vals);

        //execute blocks
        System.out.println("init... \n");
        String currentState = states.firstKey();
        List<Instruction> instructions = states.get(currentState);

        System.out.println(currentState);
        System.out.println(instructions);

        System.out.println("Executing... \n");

        int i =0;
        while(instructions != null){
            for(; i<instructions.size(); i++){
                Instruction inst = instructions.get(i);
//                System.out.println("cur inst: " + inst);

                if(inst.isStateChangeable() && inst.willChangeState(vals)){
                    instructions = inst.executeOn(states, vals);
                    i = 0;
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
        private ModifyCommandType type;
        private String newStateName;

        private boolean stateChangeable;

        public Instruction(String line){
            String[] parts = line.split(" ");
            if(parts[0].equalsIgnoreCase("if")){
                configureIf(parts);
            }else{
                configureModify(parts);
            }
        }

        //[Z, <-, Z, +, 1]
        public void configureModify(String[] parts){
            stateChangeable = false;
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
            stateChangeable = true;
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

        public boolean isStateChangeable() {
            return stateChangeable;
        }

        public boolean willChangeState(Map<String, Variable> vars){

            return vars.get(varName).notEqualsZero();
        }

        public List<Instruction> executeOn(Map<String, List<Instruction>> states, Map<String, Variable> vars){
            switch (type){
                case INCREMENT:
                    vars.get(varName).increment();
                    break;
                case DECREMENT:
                    vars.get(varName).decrement();
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
    }
}
