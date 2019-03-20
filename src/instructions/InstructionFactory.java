package instructions;

import instructions.types.*;

import java.util.Arrays;

public class InstructionFactory {
    public Instructable getInstruction(String line, int lineNumber) {
        String[] parts = line.split(" ");

        try {
            String possibleVarName = parts[0];

            if (parts[0].equalsIgnoreCase("if")) {
                return new Conditional(line, parts, lineNumber);
            } else if (parts[0].equalsIgnoreCase("GOTO")) {
                return new GoToMacro(line, parts, lineNumber);
            } else if (parts[2].equals("0")) {
                return new SetZeroMacro(line, parts, lineNumber);
            } else if (parts[1].equals("<-") && parts.length == 3) { //} else if (!parts[2].equals(possibleVarName) && parts.length == 3) {
                return new CopyMacroOperator(line, parts, lineNumber);
            } else if (parts[2].equals(possibleVarName) && parts[3].equals("+") && parts[4].equals("1")) {
                return new Operator(line, possibleVarName, InstructionType.INCREMENT, lineNumber);
            } else if (parts[2].equals(possibleVarName) && parts[3].equals("-") && parts[4].equals("1")) {
                return new Operator(line, possibleVarName, InstructionType.DECREMENT, lineNumber);
            }else{
                throw new IllegalArgumentException();
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Cannot create Instruction with: " + Arrays.toString(parts) + " at " + lineNumber);
        }
    }
}
