package language.memory;

import language.parse.Instruction;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;

public class LanguageRuntime {

    private final LinkedMap<String, List<Instruction>> instructionMap;
    private final VariableMemory vars;
    private Instruction previousInst;
    private int currentInstructionPosition;
    private List<Instruction> instructions;
    private String currentLabel;
    private int executionCount;
    private int instructionCount;


    public LanguageRuntime() {
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
        currentLabel = Instruction.DEFAULT_UNLABELED_LABEL;
        instructionMap.put(Instruction.DEFAULT_UNLABELED_LABEL, new ArrayList<>());
        instructions = instructionMap.get(Instruction.DEFAULT_UNLABELED_LABEL);
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

    public String lastLabeledEntered(){
        return instructionMap.lastKey();
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
        Instruction instruction = instructions.get(currentInstructionPosition++);
        previousInst = instruction;

        switch (instruction.getType()) {
            case INCREMENT:
                vars.incrementVariable(instruction.getWorkingVariable());
                break;
            case DECREMENT:
                vars.decrementVariable(instruction.getWorkingVariable());
                break;
            case ZERO:
                vars.reset(instruction.getWorkingVariable());
                break;
            case COPY:
                vars.replaceWith(instruction.getWorkingVariable(), instruction.getCopyVariable());
                break;
            case GOTO:
                // || fallthrough
            case CONDITIONAL:
                if (instruction.nextLabel(vars) != null) {
                    currentLabel = instruction.nextLabel(vars);
                    currentInstructionPosition = 0;
                    instructions = instructionMap.get(instruction.nextLabel(vars));
                }
                break;
            default:
                throw new IllegalStateException("Instruction not executable: " + instruction);
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

    public LinkedMap<String, List<Instruction>> getInstructionMap(){
        return instructionMap;
    }

    @Override
    public String toString() {
        return instructionMap.entrySet().toString();
    }
}
