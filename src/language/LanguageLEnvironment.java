package language;

import language.memory.InstructionMemory;
import language.memory.VariableMemory;
import language.parse.Instruction;
import language.parse.InstructionFactory;
import language.parse.InstructionType;

import java.util.Scanner;


public class LanguageLEnvironment {

    public final LanguageLEnvironmentViewModel vm;
    private final InstructionFactory factory;
    private final VariableMemory vars;
    private final InstructionMemory runtime;

    private boolean keepHistory;
    private final StringBuilder executionHistory;
    private final StringBuilder variableHistory;

    public LanguageLEnvironment(String program, String input) {
        this(new Scanner(program), input);
    }

    public LanguageLEnvironment(Scanner program, String input) {
        vm = new LanguageLEnvironmentViewModel(this);
        factory = new InstructionFactory();
        vars = new VariableMemory();
        runtime = new InstructionMemory(vars);
        keepHistory = true;
        initializeProgram(program);
        initializeVariables(input);
        this.executionHistory = new StringBuilder();
        this.variableHistory = new StringBuilder();
        keepHistory();
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
                if (instruction.getWorkingVariable() != null) {
                    vars.init(instruction.getWorkingVariable());
                    if (instruction.getType() == InstructionType.COPY) {
                        vars.init(instruction.getCopyVariable());
                    }
                }
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

    private void keepHistory() {
        if (!keepHistory) return;
        int exCount = getExecutionCount();
        executionHistory.append(vm.getExecutionCount())
                .append('\n')
                .append("\tPrevious Instruction:  ").append(vm.getPreviousInstruction())
                .append('\n')
                .append('\t').append(vm.getSnapshot())
                .append('\n')
                .append("\tLabel:  ").append(vm.getCurrentLabel())
                .append('\n')
                .append("\tNext Instruction:  ").append(vm.getNextInstruction())
                .append("\n\n");

        if (exCount == 0) variableHistory.append(vm.getSnapshot());
        else variableHistory.append("\n").append(vm.getSnapshot());
    }

    public void startKeepingHistory() {
        keepHistory = true;
    }

    public void stopKeepingHistory() {
        keepHistory = false;
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

    public String getExeHistory() {
        return executionHistory.toString();
    }

    public String getVariableHistory() {
        return variableHistory.toString();
    }

    public int getExecutionCount() {
        return runtime.getExecutionCount();
    }

    public int getInstructionCount() {
        return runtime.getInstructionCount();
    }

    public VariableMemory variables() {
        return vars;
    }
}
