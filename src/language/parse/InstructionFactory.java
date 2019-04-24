package language.parse;

import language.parse.decode.GodelNumber;

import java.util.Arrays;

public class InstructionFactory {
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

    private String decodeLabel(int a){
        if(a == 0) return null;

        int ith = 1;
        while((a - 5) > 0){
            a = a - 5;
            ith++;
        }

        return String.valueOf(((char) (a + 64))) + ith;
    }

    private InstructionType decodeType(int b){
        switch (b){
            case 0: return InstructionType.COPY;
            case 1: return InstructionType.INCREMENT;
            case 2: return InstructionType.DECREMENT;
            default: return InstructionType.CONDITIONAL;
        }
    }

    private String decodeVariable(int c){
        if(c <= 1) return "Y"; // page 53; ambiguity in Godel numbers.
        String var = c % 2 == 0 ? "X" : "Z";

        int ith = 1;
        while((c-2) > 1){
            c = c - 2;
            ith++;
        }
        return var + ith;
    }


    public Instruction decodeInstruction(int z) {

        GodelNumber ay = new GodelNumber(z);
        int a = ay.x;
        GodelNumber bc = new GodelNumber(ay.y);
        int b = bc.x;
        int c = bc.y;

        String label = decodeLabel(a);
        InstructionType type = decodeType(b);
        String var = decodeVariable(c-1); // page 51; if the variable V is mentioned in I, then c=#(V) - 1.

        switch (type){
            case COPY:
                return new Instruction(InstructionType.COPY, -1, var + " <- " + var, label);
            case INCREMENT:
                return new Instruction(InstructionType.INCREMENT, -1, var + " <- " + var + " + 1", label);
            case DECREMENT:
                return new Instruction(InstructionType.DECREMENT, -1, var + " <- " + var + " - 1", label);
            case CONDITIONAL:
                return new Instruction(InstructionType.CONDITIONAL, -1, "IF " + var + " !=  0 GOTO " + decodeLabel(b-2), label);
            default: throw new IllegalArgumentException("Cannot decode another type of instruction other than the primitives.");
        }
    }


}
