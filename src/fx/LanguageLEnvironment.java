package fx;

import instructions.Instructable;
import instructions.InstructionFactory;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.*;


public class LanguageLEnvironment {
    private static final String DEFAULT_UNLABELED_STATE = "Unlabeled Instruction";

    private InstructionFactory factory;
    private LinkedMap<String, List<Instructable>> states;
    private Map<String, Integer> vars;
    private List<Instructable> instructions;

    private String currentLabel;

    private int currentInstructionPosition;
    private int instructionCount;
    private int executionCount;
    private Instructable previousInst;

    public LanguageLEnviormentViewModel vm;

    public String getExeHistory() {
        return executionHistory.toString();
    }

    public String getVariableHistory() {
        return variableHistory.toString();
    }

    private StringBuilder executionHistory;
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
        instructions = states.get(currentLabel);
        initializeInput(input);
        currentInstructionPosition = 0;
        executionCount = 0;
        this.executionHistory = new StringBuilder();
        this.variableHistory = new StringBuilder();
        keepHistory();
    }

    private void keepHistory() {
        int exCount = getExecutionCount();
        executionHistory.append(vm.getExecutionCount());
        executionHistory.append('\n');
        executionHistory.append("\tPrevious Instruction:  ").append(vm.getPreviousInstruction());
        executionHistory.append('\n');
        executionHistory.append('\t').append(vm.getSnapshot());
        executionHistory.append('\n');
        executionHistory.append("\tLabel:  ").append(vm.getCurrentLabel());
        executionHistory.append('\n');
        executionHistory.append("\tNext Instruction:  ").append(vm.getNextInstruction());
        executionHistory.append("\n\n");

        if (exCount == 0) variableHistory.append(vm.getSnapshot());
        else variableHistory.append("\n").append(vm.getSnapshot());
    }

    public int getExecutionCount() {
        return executionCount;
    }

    private void initializeProgram(Scanner p) {
        //initialize states
        instructionCount = 0;
        states.put(DEFAULT_UNLABELED_STATE, new ArrayList<>());
        currentLabel = DEFAULT_UNLABELED_STATE;
        while (p.hasNextLine()) {
            String line = p.nextLine();
            if (line.isEmpty() || line.contains("//")) continue;
            if (line.contains("[") && line.contains("]")) {
                String state = line.replace("[", "").replace("]", "");
                if (instructionCount == 0) currentLabel = state;
                states.put(state, new ArrayList<>());
            } else {
                Instructable instruction = factory.getInstruction(line, ++instructionCount);
                states.get(states.lastKey()).add(instruction);
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
            if (val < 0) throw new IllegalStateException("Variables cannot be negative. " + kv[0]);
            vars.put(kv[0], val);
        }
    }

    public boolean hasInstructions() {
        return instructions != null && currentInstructionPosition < instructions.size();
    }

    public void executeNext() {

        if (!hasInstructions()) throw new IllegalStateException("There are no instructions to execute");

        executionCount++;
        Instructable inst = instructions.get(currentInstructionPosition++);
        previousInst = inst;

        if (inst.nextState(states, vars) != null) {
            currentLabel = inst.nextState(states, vars);
            instructions = inst.executeOn(states, vars);
            currentInstructionPosition = 0;
        } else {
            inst.executeOn(states, vars);
        }

        checkAndUpdateState();
        keepHistory();
    }

    private void checkAndUpdateState() {
        if (!hasInstructions()) {
            int currentLabelIndex = states.indexOf(currentLabel);
            if (currentLabelIndex != -1 && currentLabelIndex != states.size() - 1) {
                currentInstructionPosition = 0;
                currentLabel = states.get(currentLabelIndex + 1);
                instructions = states.get(currentLabel);
            }
        }
    }

    public String getCurrentLabel() {
        return currentLabel;
    }

    public Instructable getNextInstruction() {
        if (!hasInstructions()) return null;
        return instructions.get(currentInstructionPosition);
    }

    public Instructable getPrevInstruction() {
        return previousInst;
    }


    public int getInstructionCount() {
        return instructionCount;
    }

    public Map<String, Integer> variables() {
        return vars;
    }
}
