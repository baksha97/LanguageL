package language.memory;

import language.parse.Instruction;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;

public class InstructionMemory {
    private static final String DEFAULT_UNLABELED_LABEL = "Unlabeled Instruction";

    private final LinkedMap<String, List<Instruction>> instructionMap;

    private Instruction previousInst;
    private int currentInstructionPosition;
    private List<Instruction> instructions;
    private String currentLabel;
    private int executionCount;
    private int instructionCount;
    private final VariableMemory vars;


    public InstructionMemory(VariableMemory vars) {
        this.vars = vars;
        instructionMap = new LinkedMap<>();
        currentInstructionPosition = 0;
        executionCount = 0;
        instructionCount = 0;
        setupDefaultLabel();
    }

    private void setupDefaultLabel() {
        currentLabel = DEFAULT_UNLABELED_LABEL;
        instructionMap.put(DEFAULT_UNLABELED_LABEL, new ArrayList<>());
        instructions = instructionMap.get(DEFAULT_UNLABELED_LABEL);
    }


    public void addLabel(String label) {
        if (!instructionMap.containsKey(label)) instructionMap.put(label, new ArrayList<>());
    }


    public void addInstruction(String label, Instruction inst) {
        addLabel(label);
        verifyInstructionPosition();
        instructionMap.get(label).add(inst);
        instructionCount++;

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

        if (inst.nextLabel(vars) != null) {
            currentLabel = inst.nextLabel(vars);
            instructions = inst.executeOn(instructionMap, vars);
            currentInstructionPosition = 0;
        } else {
            inst.executeOn(instructionMap, vars);
        }

        verifyInstructionPosition();
    }

    private void verifyInstructionPosition() {
        if (!hasInstructions()) {
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


}
