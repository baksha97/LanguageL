import instructions.GoToMacro;
import instructions.Instructable;
import instructions.InstructionFactory;
import instructions.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import javax.xml.crypto.NoSuchMechanismException;
import java.util.*;


public class LanguageLEnvironment {
    private InstructionFactory factory;
    private LinkedMap<String, List<Instructable>> states;
    private HashMap<String, Integer> vars;
    private List<Instructable> instructions;

    private int pos;

    public LanguageLEnvironment(String program, String input){
        factory = new InstructionFactory();
        states = new LinkedMap<>();
        vars = new HashMap<>();
        initializeProgram(program);
        initializeInput(input);
        instructions = states.get(states.firstKey());
        pos = 0;
    }

    public LanguageLEnvironment(Scanner program, String input){
        factory = new InstructionFactory();
        states = new LinkedMap<>();
        vars = new HashMap<>();
        initializeProgram(program);
        initializeInput(input);
        instructions = states.get(states.firstKey());
        pos = 0;
    }

    private void initializeProgram(String program){
        Scanner in = new Scanner(program);
        //initialize states
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.isEmpty()) continue;
            if(line.contains("[")){
                states.put(line.replace("[","").replace("]",""), new ArrayList<>());
            }else{
                Instructable instruction = factory.getInstruction(line);
                states.get(states.lastKey()).add(instruction);
                if(instruction.getType() != InstructionType.GOTO_MACRO) vars.put(instruction.getVarName(), 0);
            }
        }
        in.close();
    }

    private void initializeProgram(Scanner p){
        //initialize states
        while(p.hasNextLine()){
            String line = p.nextLine();
            if(line.isEmpty()) continue;
            if(line.contains("[")){
                states.put(line.replace("[","").replace("]",""), new ArrayList<>());
            }else{
                Instructable instruction = factory.getInstruction(line);
                states.get(states.lastKey()).add(instruction);
                if(instruction.getType() != InstructionType.GOTO_MACRO) vars.put(instruction.getVarName(), 0);
            }
        }
    }
    private void initializeInput(String input){
        String[] inputs = input.split(",");
        for (String s : inputs) {
            String[] kv = s.split("=");
            vars.put(kv[0], Integer.parseInt(kv[1]));
        }
    }

    public boolean hasInstructions(){
        return instructions != null && pos < instructions.size();
    }

    public void executeNext(){

        if(!hasInstructions()) throw new NoSuchMechanismException("There are no instructions to execute");

        Instructable inst = instructions.get(pos++);

        if(inst instanceof GoToMacro){
            String newStateName = ((GoToMacro) inst).getNewStateName();
            if(!states.containsKey(newStateName)){
                instructions = null;
                pos = 0;
            }
        }

        if(inst.getGoToMacroNewState(states) != null){
            instructions = inst.getGoToMacroNewState(states);
            pos = 0;
        }

        if(inst.willChangeState(states, vars)){
            instructions = inst.executeOn(states, vars);
            pos = 0;
        }
        System.out.println(inst);
        inst.executeOn(states, vars);

    }

    public LinkedMap<String, List<Instructable>> states(){
        return states;
    }

    public HashMap<String, Integer> variables(){
        return vars;
    }
}
