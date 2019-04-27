package language.parse.numeric;

import language.parse.Instruction;
import language.parse.InstructionType;

import java.math.BigInteger;

public final class Decoder {

    private String decodeLabel(BigInteger a){
        int label = a.intValue();
        if(label == 0) return null;
        int ith = 1;
        while((label - 5) > 0){
            label = label - 5;
            ith++;
        }
        return String.valueOf((char) (label + 64)) + ith;
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
        c = c + 1; // c = #(V) - 1
        if(c == 1) return "Y"; // page 53; ambiguity in Godel numbers.
        String var = c % 2 == 0 ? "X" : "Z";

        int ith = 1;
        while((c-2) > 1){
            c = c - 2;
            ith++;
        }
        return var + ith;
    }



    public Instruction decodeInstruction(BigInteger z) {

        GodelPair ay = new GodelPair(z);
        BigInteger a = ay.getX();
        GodelPair bc = new GodelPair(ay.getY());
        BigInteger b = bc.getX();
        BigInteger c = bc.getY();

        String godelNotation = "<" + a + ", " + ay.getY() + "> = "
                + "<" + a + ", <" + b + ", " + c + ">>";

        String label = decodeLabel(a);
        InstructionType type = decodeType(b.intValueExact());
        String var = decodeVariable(c.intValueExact()); // page 51; if the variable V is mentioned in I, then c=#(V) - 1.

        switch (type){
            case COPY:
                return new Instruction(InstructionType.COPY, var + " <- " + var, label, godelNotation);
            case INCREMENT:
                return new Instruction(InstructionType.INCREMENT,(var + " <- " + var + " + 1"), label, godelNotation);
            case DECREMENT:
                return new Instruction(InstructionType.INCREMENT,(var + " <- " + var + " - 1"), label, godelNotation);
            case CONDITIONAL:
                return new Instruction(InstructionType.CONDITIONAL, ("IF " + var + " != 0 GOTO " + label), label, godelNotation);
            default: throw new IllegalArgumentException("Cannot decode another type of instruction other than the primitives.");
        }
    }
}
