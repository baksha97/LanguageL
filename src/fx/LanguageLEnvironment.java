package fx;

import language.RuntimeMemory;
import language.parse.InstructionFactory;
import language.Instruction;
import language.memory.VariableMemory;
import java.util.*;


public class LanguageLEnvironment {

    private InstructionFactory factory;
    private VariableMemory vars;
    private RuntimeMemory runtime;


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
        vars = new VariableMemory();
        runtime = new RuntimeMemory(vars);
        initializeProgram(program);
        initializeVariables(input);
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
        return runtime.getExecutionCount();
    }

    private void initializeProgram(Scanner p) {
        //initialize instructionMap
        int instructionCount = 0;
        while (p.hasNextLine()) {
            String line = p.nextLine();
            if (line.isEmpty() || line.contains("//")) continue;
            if (line.contains("[") && line.contains("]")) {
                String label = line.replace("[", "").replace("]", "");
                runtime.addLabel(label);
            } else {
                Instruction instruction = factory.getInstruction(line, ++instructionCount);
                runtime.addInstructionToLastLabel(instruction);
                if(instruction.getWorkingVariable() != null) vars.init(instruction.getWorkingVariable());
            }
        }
        p.close();
    }

    private void initializeVariables(String input) {
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
        return runtime.hasInstructions();
    }

    public void executeNext() {
        runtime.executeNext();
        keepHistory();
    }


    public String getCurrentLabel() {
        return runtime.getCurrentLabel();
    }

    public Instruction getNextInstruction() {
        return runtime.getNextInstruction();
    }

    public Instruction getPrevInstruction() {
        return runtime.getPrevInstruction();
    }


    public int getInstructionCount() {
        return runtime.getInstructionCount();
    }

    public VariableMemory variables() {
        return vars;
    }
}
