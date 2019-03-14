package fx;

import instructions.Instructable;
import instructions.InstructionFactory;
import org.apache.commons.collections4.map.LinkedMap;

import javax.xml.crypto.NoSuchMechanismException;
import java.util.*;


public class LanguageLEnvironment {
    private static final String DEFAULT_UNLABLED_STATE = "Unlabeled Instruction";

    private InstructionFactory factory;
    private LinkedMap<String, List<Instructable>> states;
    private Map<String, Integer> vars;
    private List<Instructable> instructions;

    private String currentState;

    private int pos;
    private int instructionCount;
    private int executionCount;
    private Instructable previousInst;

    public LanguageLEnvironment(String program, String input) {
        this(new Scanner(program), input);
    }

    public LanguageLEnvironment(Scanner program, String input) {
        factory = new InstructionFactory();
        states = new LinkedMap<>();
        vars = new TreeMap<>();
        initializeProgram(program);
        initializeInput(input);
        instructions = states.get(currentState);
        pos = 0;
        executionCount = 0;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    private void initializeProgram(Scanner p) {
        //initialize states
        instructionCount = 0;
        states.put(DEFAULT_UNLABLED_STATE, new ArrayList<>());
        currentState = DEFAULT_UNLABLED_STATE;
        while (p.hasNextLine()) {
            String line = p.nextLine();
            if (line.isEmpty() || line.contains("//")) continue;
            if (line.contains("[") && line.contains("]")) {
                String state = line.replace("[", "").replace("]", "");
                if(instructionCount == 0) currentState = state;
                states.put(state, new ArrayList<>());
            } else {
                Instructable instruction = factory.getInstruction(line);
                states.get(states.lastKey()).add(instruction);
                instructionCount++;
                if (instruction.getVarName() != null) vars.put(instruction.getVarName(), 0);
            }
        }
        p.close();
    }

    private void initializeInput(String input) {
        input = input.replace(" ", "");
        String[] inputs = input.split(",");
        for (String s : inputs) {
            String[] kv = s.split("=");
            int val = Integer.parseInt(kv[1]);
            if(val < 0) throw new IllegalStateException("Variables cannot be negative. " + kv[0]);
            vars.put(kv[0], val);
        }
    }

    public boolean hasInstructions() {
        return instructions != null && pos < instructions.size();
    }

    public void executeNext() {

        if (!hasInstructions()) throw new NoSuchMechanismException("There are no instructions to execute");

        executionCount++;
        Instructable inst = instructions.get(pos++);
        previousInst = inst;

        if (inst.nextState(states, vars) != null) {
            currentState = inst.nextState(states, vars);
            instructions = inst.executeOn(states, vars);
            pos = 0;
        } else {
            inst.executeOn(states, vars);
        }
    }

    public String getCurrentState() {
        return currentState;
    }

    public Instructable getNextInstruction() {
        if (!hasInstructions()) return null;
        return instructions.get(pos);
    }

    public Instructable getPrevInstruction() {
        return previousInst;
    }

    public LinkedMap<String, List<Instructable>> states() {
        return states;
    }

    public int getInstructionCount(){
        return instructionCount;
    }
    public Map<String, Integer> variables() {
        return vars;
    }
}
