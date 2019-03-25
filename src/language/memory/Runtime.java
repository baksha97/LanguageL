package language.memory;

import language.parse.Instruction;
import language.parse.InstructionType;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;

public class Runtime {
    private static final String DEFAULT_UNLABELED_LABEL = "Unlabeled Instruction";

    private final LinkedMap<String, List<Instruction>> instructionMap;
    private final VariableMemory vars;
    private Instruction previousInst;
    private int currentInstructionPosition;
    private List<Instruction> instructions;
    private String currentLabel;
    private int executionCount;
    private int instructionCount;


    public Runtime() {
        this.vars = new VariableMemory();
        instructionMap = new LinkedMap<>();
        currentInstructionPosition = 0;
        executionCount = 0;
        instructionCount = 0;
        setupDefaultLabel();
    }

    public void setInputVariable(String var, int value) {
        vars.put(var, value);
    }

    private void initVariablesFromInstruction(Instruction instruction) {
        if (instruction.getWorkingVariable() != null) {
            vars.initIfAbsent(instruction.getWorkingVariable());
        }
        if (instruction.getCopyVariable() != null) {
            vars.initIfAbsent(instruction.getCopyVariable());
        }
    }

    private void setupDefaultLabel() {
        currentLabel = DEFAULT_UNLABELED_LABEL;
        instructionMap.put(DEFAULT_UNLABELED_LABEL, new ArrayList<>());
        instructions = instructionMap.get(DEFAULT_UNLABELED_LABEL);
    }

    public void addLabel(String label) {
        if (!instructionMap.containsKey(label)) instructionMap.put(label, new ArrayList<>());
    }

    //I am implementing it this way although not publicly used to allow input such as "[A]: GOTO B" in the future.
    public void addInstruction(String label, Instruction inst) {
        addLabel(label);
        initVariablesFromInstruction(inst);
        instructionMap.get(label).add(inst);
        instructionCount++;
        verifyInstructionPosition();
    }

    public void addInstructionToLastLabel(Instruction inst) {
        addInstruction(instructionMap.lastKey(), inst);
    }

    public boolean hasInstructions() {
        return instructions != null && currentInstructionPosition < instructions.size();
    }

    public void executeNext() {
        if (!hasInstructions()) throw new IllegalStateException("There are no instructions to execute");
        executionCount++;
        Instruction inst = instructions.get(currentInstructionPosition++);
        previousInst = inst;

        switch (inst.getType()) {
            case INCREMENT:
                vars.incrementVariable(inst.getWorkingVariable());
                break;
            case DECREMENT:
                vars.decrementVariable(inst.getWorkingVariable());
                break;
            case ZERO:
                vars.reset(inst.getWorkingVariable());
                break;
            case COPY:
                vars.replaceWith(inst.getWorkingVariable(), inst.getCopyVariable());
                break;
        }

        boolean canChangeLabel = inst.getType() == InstructionType.GOTO || inst.getType() == InstructionType.CONDITIONAL;

        if (canChangeLabel && inst.nextLabel(vars) != null) {
            currentLabel = inst.nextLabel(vars);
            currentInstructionPosition = 0;
            instructions = instructionMap.get(inst.nextLabel(vars));
        }

        verifyInstructionPosition();
    }

    private void verifyInstructionPosition() {
        boolean hasInstruction = instructions != null && currentInstructionPosition < instructions.size();
        if (!hasInstruction) {
            int currentLabelIndex = instructionMap.indexOf(currentLabel);
            if (currentLabelIndex != -1 && currentLabelIndex != instructionMap.size() - 1) {
                currentInstructionPosition = 0;
                currentLabel = instructionMap.get(currentLabelIndex + 1);
                instructions = instructionMap.get(currentLabel);
            }
        }
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public String getCurrentLabel() {
        return currentLabel;
    }

    public Instruction getPrevInstruction() {
        return previousInst;
    }

    public Instruction getNextInstruction() {
        if (!hasInstructions()) return null;
        return instructions.get(currentInstructionPosition);
    }

    public int getInstructionCount() {
        return instructionCount;
    }

    public VariableMemory variables() {
        return vars;
    }

    @Override
    public String toString() {
        return vars.toString();
    }
}
