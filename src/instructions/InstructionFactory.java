package instructions;

import instructions.types.*;

import java.util.Arrays;

public class InstructionFactory {
    public Instructable getInstruction(String line) {
        String[] parts = line.split(" ");

        String possibleVarName = parts[0];

        if (parts[0].equalsIgnoreCase("if")) {
            return new Conditional(line, parts);
        } else if (parts[0].equalsIgnoreCase("GOTO")) {
            return new GoToMacro(line, parts);
        } else if (parts[2].equals("0")) {
            return new SetZeroMacro(line, parts);
        } else if (!parts[2].equals(possibleVarName) && parts.length == 3) {
            return new CopyMacroOperator(line, parts);
        } else if (parts[2].equals(possibleVarName) && parts[3].equals("+") && parts[4].equals("1")) {
            return new Operator(line, possibleVarName, InstructionType.INCREMENT);
        } else if (parts[2].equals(possibleVarName) && parts[3].equals("-") && parts[4].equals("1")) {
            return new Operator(line, possibleVarName, InstructionType.DECREMENT);
        } else {
            throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts));
        }
    }
}
