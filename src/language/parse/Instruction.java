package language.parse;

import language.memory.VariableMemory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Instruction {

    private final InstructionType type;
    private final int lineNumber;
    private final String line;
    private final String[] parts;

    public Instruction(InstructionType type, int lineNumber, String line) {
        this.type = type;
        this.lineNumber = lineNumber;
        this.line = line;
        this.parts = line.split(" ");
    }

    public List<Instruction> executeOn(Map<String, List<Instruction>> states, VariableMemory vars) {
        String variable;
        String variableToCopy;
        String newState;
        switch (this.type) {
            case INCREMENT:
                variable = parts[LanguageIndices.VARIABLE];
                vars.incrementVariable(variable);
                break;
            case DECREMENT:
                variable = parts[LanguageIndices.VARIABLE];
                vars.decrementVariable(variable);
                break;
            case CONDITIONAL:
                variable = parts[LanguageIndices.Conditional.VARIABLE_TO_CHECK];
                newState = parts[LanguageIndices.Conditional.POSSIBLE_NEW_STATE];
                if (vars.variableNotZero(variable)) return states.get(newState);
                break;
            case GOTO:
                newState = parts[LanguageIndices.GoTo.NEW_STATE];
                return states.get(newState);
            case SET_ZERO:
                variable = parts[LanguageIndices.VARIABLE];
                vars.resetVariable(variable);
                break;
            case COPY:
                variable = parts[LanguageIndices.VARIABLE];
                variableToCopy = parts[LanguageIndices.Copy.VARIABLE_TO_COPY];
                vars.copyVariableValue(variable, variableToCopy);
                break;
        }
        return null;
    }

    public String getWorkingVariable() {
        if (type == InstructionType.GOTO) return null;
        if (type == InstructionType.CONDITIONAL) return parts[LanguageIndices.Conditional.VARIABLE_TO_CHECK];
        return parts[LanguageIndices.VARIABLE];
    }

    public String getCopyVariable() {
        if (type != InstructionType.COPY) return null;
        return parts[LanguageIndices.Copy.VARIABLE_TO_COPY];
    }

    public String nextLabel(VariableMemory vars) {
        if (type == InstructionType.GOTO) {
            return parts[LanguageIndices.GoTo.NEW_STATE];
        } else if (type == InstructionType.CONDITIONAL) {
            String variable = parts[LanguageIndices.Conditional.VARIABLE_TO_CHECK];
            String newState = parts[LanguageIndices.Conditional.POSSIBLE_NEW_STATE];
            if (vars.variableNotZero(variable)) {
                return newState;
            }
        }

        return null;
    }

    public InstructionType getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return Arrays.toString(parts);
    }
}
