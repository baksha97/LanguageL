package language.parse;

import language.parse.numeric.Decoder;

import java.util.Arrays;

public class InstructionFactory {

    private Decoder decoder;

    public InstructionFactory(){
        this.decoder = new Decoder();
    }

    public Instruction parseInstruction(String label, String line, int lineNumber) {
        String[] parts = line.split(" ");

        try {
            String possibleVarName = parts[0];
            if (parts[0].equalsIgnoreCase("if"))
                return new Instruction(InstructionType.CONDITIONAL, lineNumber, line, label);
            else if (parts[0].equalsIgnoreCase("GoTo"))
                return new Instruction(InstructionType.GOTO, lineNumber, line, label);
            else if (parts[2].equals("0")) return new Instruction(InstructionType.ZERO, lineNumber, line, label);
            else if (parts.length == 3)
                return new Instruction(InstructionType.COPY, lineNumber, line, label);
            else if (parts[2].equals(possibleVarName) && parts[3].equals("+") && parts[4].equals("1"))
                return new Instruction(InstructionType.INCREMENT, lineNumber, line, label);
            else if (parts[2].equals(possibleVarName) && parts[3].equals("-") && parts[4].equals("1"))
                return new Instruction(InstructionType.DECREMENT, lineNumber, line, label);
            throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts) + " at " + lineNumber + " for label " + label);
        }
    }

}
