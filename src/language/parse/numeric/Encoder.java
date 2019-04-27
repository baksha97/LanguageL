package language.parse.numeric;

import language.memory.LanguageRuntime;
import language.parse.Instruction;

import java.math.BigInteger;
import java.util.ArrayList;

public class Encoder {
    public ArrayList<BigInteger> getInstructionNumbers() {
        return instructionNumbers;
    }

    private ArrayList<BigInteger> instructionNumbers;

    public Encoder(LanguageRuntime rt){
        instructionNumbers = new ArrayList<>();
        rt.getInstructionMap().forEach((label, list) ->{
            list.forEach(item -> instructionNumbers.add(encode(item)));
        });




    }

    public BigInteger encode(Instruction inst){
        int a = encodeLabel(inst);
        int b = encodeType(inst);
        int c = encodeVariable(inst);
        GodelPair bc = new GodelPair(b,c);
        GodelPair ay = new GodelPair(BigInteger.valueOf(a), bc.getZ());

        return ay.getZ();
    }


    private int encodeLabel(Instruction inst){
        if(inst.getLabel() == null || inst.getLabel().equals(Instruction.DEFAULT_UNLABELED_LABEL)) return 0;
        return encodeLabel(inst.getLabel());
    }

    private int encodeLabel(String label){
        if(label.length() == 1) label = label + '1';

        char letter = label.charAt(0);
        int ith = Integer.parseInt(label.substring(1));
        int startingCharNumber = Math.abs('A' - letter) + 1;

        return startingCharNumber + (ith-1) * 5;
    }

    private int encodeType(Instruction inst){
        switch (inst.getType()){
            case COPY: return 0;
            case INCREMENT: return 1;
            case DECREMENT: return 2;
            case CONDITIONAL: return encodeLabel(inst.conditionalLabel()) + 2;
            default: throw new IllegalArgumentException("Cannot encode another type of instruction other than the 4 primitives.");
        }
    }

    private int encodeVariable(Instruction inst){
        String var = inst.getWorkingVariable();

        if(var.length() == 1) var = var + '1'; //always have ith of var on substring
        int ith = Integer.parseInt(var.substring(1));

        int offset = 0;

        if(var.charAt(0) == 'Y') return offset;
        else if(var.charAt(0) == 'X') offset = offset + 1;
        else if(var.charAt(0) == 'Z') offset = offset + 2;
        else throw new IllegalArgumentException("Cannot encode variables other than X, Z, & Y.");
        System.out.println(offset);

        return offset + (ith - 1) * 2;
    }
}
