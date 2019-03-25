package language.parse;

import language.memory.VariableMemory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Instruction {

    private final InstructionType type;
    private final int instructionNumber;
    private final String line;
    private final String[] statement;

    public Instruction(InstructionType type, int instructionNumber, String line) {
        this.type = type;
        this.instructionNumber = instructionNumber;
        this.line = line;
        this.statement = line.split(" ");
    }

    public boolean canChangeLabel(){
        return getType() == InstructionType.GOTO || getType() == InstructionType.CONDITIONAL;
    }

    public String getWorkingVariable() {
        if (type == InstructionType.GOTO) return null;
        if (type == InstructionType.CONDITIONAL) return statement[LanguageIndices.Conditional.VARIABLE_TO_CHECK];
        return statement[LanguageIndices.VARIABLE];
    }

    public String getCopyVariable() {
        if (type != InstructionType.COPY) return null;
        return statement[LanguageIndices.Copy.VARIABLE_TO_COPY];
    }

    private String conditionalLabel() {
        if (type == InstructionType.CONDITIONAL)
            return statement[LanguageIndices.Conditional.POSSIBLE_NEW_LABEL];
        return null;
    }

    public String nextLabel(VariableMemory vars) {
        if (type == InstructionType.GOTO) {
            return statement[LanguageIndices.GoTo.NEW_LABEL];
        } else if (type == InstructionType.CONDITIONAL && vars.isNotZero(getWorkingVariable())) {
            return conditionalLabel();
        }
        return null;
    }

    public InstructionType getType() {
        return type;
    }

    public int getInstructionNumber() {
        return instructionNumber;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return Arrays.toString(statement);
    }
}
