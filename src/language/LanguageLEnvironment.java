package language;

import language.memory.LanguageRuntime;
import language.memory.VariableMemory;
import language.parse.Instruction;
import language.parse.InstructionFactory;

import java.util.Arrays;
import java.util.Scanner;


public class LanguageLEnvironment {

    public final LanguageLEnvironmentViewModel vm;
    private final InstructionFactory factory;

    public LanguageRuntime getRuntime() {
        return runtime;
    }

    private final LanguageRuntime runtime;
    private final StringBuilder executionHistory;
    private final StringBuilder variableHistory;
    private boolean keepHistory;

    public LanguageLEnvironment(String program, String input) {
        this(new Scanner(program), input);
    }

    public LanguageLEnvironment(Scanner program, String input) {
        vm = new LanguageLEnvironmentViewModel(this);
        factory = new InstructionFactory();
        runtime = new LanguageRuntime();
        keepHistory = true;
        executionHistory = new StringBuilder();
        variableHistory = new StringBuilder();
        initializeVariables(input);
        initializeProgram(program);
        keepHistory();
    }

    private void initializeProgram(Scanner p) {
        //initialize instructionMap
        int instructionCount = 0;
        while (p.hasNextLine()) {
            String line = p.nextLine().trim();
            if (line.isEmpty() || line.contains("//")) continue;

            if (line.contains("[") && line.contains("]")) {
                String label = line.replace("[", "").replace("]", "");
                runtime.addLabel(label);
            } else {
                Instruction instruction = factory.parseInstruction(runtime.lastLabeledEntered(), line, ++instructionCount);
                runtime.addInstructionToLastLabel(instruction);
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
            if (val < 0) throw new IllegalArgumentException("Variables cannot be negative. " + kv[0]);
            runtime.setInputVariable(kv[0], val);
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
        return runtime.variables();
    }

    @Override
    public String toString() {
        return runtime.toString();
    }
}
