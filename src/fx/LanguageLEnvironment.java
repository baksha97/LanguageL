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

    private String currentLabel;

    private int pos;
    private int instructionCount;
    private int executionCount;
    private Instructable previousInst;

    public LanguageLEnviormentViewModel vm;

    public String getExeHistory() {
        return exexutionHistory.toString();
    }

    public String getVariableHistory() {
        return variableHistory.toString();
    }

    private StringBuilder exexutionHistory;
    private StringBuilder variableHistory;

    public LanguageLEnvironment(String program, String input) {
        this(new Scanner(program), input);
    }

    public LanguageLEnvironment(Scanner program, String input) {
        vm = new LanguageLEnviormentViewModel(this);
        factory = new InstructionFactory();
        states = new LinkedMap<>();
        vars = new TreeMap<>();
        initializeProgram(program);
        initializeInput(input);
        instructions = states.get(currentLabel);
        pos = 0;
        executionCount = 0;
        this.exexutionHistory = new StringBuilder();
        this.variableHistory = new StringBuilder();
        keepHistory();
    }

    private void keepHistory(){
        int exCount = getExecutionCount();
        exexutionHistory.append(vm.getExecutionCount());
        exexutionHistory.append('\n');
        exexutionHistory.append("\tPrevious Instruction:  " +vm.getPreviousInstruction());
        exexutionHistory.append('\n');
        exexutionHistory.append('\t' +vm.getSnapshot());
        exexutionHistory.append('\n');
        exexutionHistory.append("\tLabel:  " +vm.getCurrentLabel());
        exexutionHistory.append('\n');
        exexutionHistory.append("\tNext Instruction:  " +vm.getNextInstruction());
        exexutionHistory.append("\n\n");

        if(exCount == 0) variableHistory.append(vm.getSnapshot());
        else variableHistory.append("\n"+ vm.getSnapshot());
    }

    public int getExecutionCount() {
        return executionCount;
    }

    private void initializeProgram(Scanner p) {
        //initialize states
        instructionCount = 0;
        states.put(DEFAULT_UNLABLED_STATE, new ArrayList<>());
        currentLabel = DEFAULT_UNLABLED_STATE;
        while (p.hasNextLine()) {
            String line = p.nextLine();
            if (line.isEmpty() || line.contains("//")) continue;
            if (line.contains("[") && line.contains("]")) {
                String state = line.replace("[", "").replace("]", "");
                if(instructionCount == 0) currentLabel = state;
                states.put(state, new ArrayList<>());
            } else {
                Instructable instruction = factory.getInstruction(line, ++instructionCount);
                states.get(states.lastKey()).add(instruction);
//                instructionCount++;
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
            currentLabel = inst.nextState(states, vars);
            instructions = inst.executeOn(states, vars);
            pos = 0;
        } else {
            inst.executeOn(states, vars);
        }

        keepHistory();
    }

    public String getCurrentLabel() {
        return currentLabel;
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
