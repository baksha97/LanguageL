package language.parse;

import language.memory.VariableMemory;

import java.util.Arrays;

public class Instruction {

    public static final String DEFAULT_UNLABELED_LABEL = "Unlabeled Instruction";

    private final String label;
    private final InstructionType type;
    private final int instructionNumber;
    private final String line;
    private final String[] statement;
    private final String godelNotation;

    public Instruction(InstructionType type, int instructionNumber, String line, String label) {
        this.label = label;
        this.type = type;
        this.instructionNumber = instructionNumber;
        this.line = line;
        this.statement = line.split(" ");
        godelNotation = null;
    }

    //this constructor makes things a bit awkward by only using it temporarily for decoding
    //should implement another way when possible.
    public Instruction(InstructionType type, String line, String label, String godelNotation) {
        this.label = label;
        this.instructionNumber = -1;
        this.type = type;
        this.line = line;
        this.statement = line.split(" ");
        this.godelNotation = godelNotation;
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

    public String getGodelNotation(){
        if (godelNotation == null) throw new IllegalStateException("Godel notation is null, you cannot access it since it has not been decoded.");
        return godelNotation;
    }

    public String getLabel(){ return label;}
    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return Arrays.toString(statement);
    }
}
