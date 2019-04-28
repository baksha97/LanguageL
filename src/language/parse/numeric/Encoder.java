package language.parse.numeric;

import language.memory.LanguageRuntime;
import language.parse.Instruction;

import java.math.BigInteger;
import java.util.ArrayList;

public class Encoder {

    private ArrayList<EncoderData> data;


    public Encoder(LanguageRuntime rt){
        data = new ArrayList<>();
        rt.getInstructionMap().forEach((label, list) ->{
            list.forEach(item -> {
                data.add(encode(item));
            });
        });

    }

    public EncoderData encode(Instruction inst){
        int a = encodeLabel(inst);
        int b = encodeType(inst);
        int c = encodeVariable(inst);
        GodelPair bc = new GodelPair(b,c);
        GodelPair ay = new GodelPair(BigInteger.valueOf(a), bc.getZ());

        return new EncoderData(inst, ay, bc, ay.getZ());
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
            case DUMMY: return 0;
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
        return offset + (ith - 1) * 2;
    }

    public ArrayList<EncoderData> getEncoderData(){
        return this.data;
    }

    public static class EncoderData{
        Instruction inst;

        public BigInteger getInstructionNumber() {
            return instructionNumber;
        }

        BigInteger instructionNumber;
        GodelPair ay;
        GodelPair bc;

        private EncoderData(Instruction inst, GodelPair ay, GodelPair bc, BigInteger instructionNumber) {
            this.inst = inst;
            this.instructionNumber = instructionNumber;
            this.ay = ay;
            this.bc = bc;
        }

        @Override
        public String toString() {
            return  "Instruction=" + inst +
                    "\n\tinstructionNumber=" + instructionNumber +
                    "\n\tay=" + ay +
                    "\n\tbc=" + bc +
                    "\n";
        }
    }
}
