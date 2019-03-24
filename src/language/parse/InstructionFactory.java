package language.parse;

import java.util.Arrays;

public class InstructionFactory {
    public Instruction getInstruction(String line, int lineNumber) {
        String[] parts = line.split(" ");

        try {
            String possibleVarName = parts[0];
            if (parts[0].equalsIgnoreCase("if"))
                return new Instruction(InstructionType.CONDITIONAL, lineNumber, line);
            else if (parts[0].equalsIgnoreCase("GoTo"))
                return new Instruction(InstructionType.GOTO, lineNumber, line);
            else if (parts[2].equals("0")) return new Instruction(InstructionType.SET_ZERO, lineNumber, line);
            else if (parts.length == 3)
                return new Instruction(InstructionType.COPY, lineNumber, line);
            else if (parts[2].equals(possibleVarName) && parts[3].equals("+") && parts[4].equals("1"))
                return new Instruction(InstructionType.INCREMENT, lineNumber, line);
            else if (parts[2].equals(possibleVarName) && parts[3].equals("-") && parts[4].equals("1"))
                return new Instruction(InstructionType.DECREMENT, lineNumber, line);
            throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts) + " at " + lineNumber);
        }
    }
}
