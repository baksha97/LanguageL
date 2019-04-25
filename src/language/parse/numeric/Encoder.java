package language.parse.numeric;

import language.parse.Instruction;

public class Encoder {

    public int encodeLabel(Instruction inst){
        if(inst.getLabel() == null) return 0;
        return encodeLabel(inst.getLabel());
    }

    private int encodeLabel(String label){
        if(label.length() == 1) label = label + '1';

        char letter = label.charAt(0);
        int ith = Integer.parseInt(label.substring(1));
        int startingCharNumber = Math.abs('A' - letter) + 1;

        return startingCharNumber + (ith-1) * 5;
    }

    public int encodeType(Instruction inst){
        switch (inst.getType()){
            case COPY: return 0;
            case INCREMENT: return 1;
            case DECREMENT: return 2;
            case CONDITIONAL: return encodeLabel(inst.conditionalLabel()) + 2;
            default: throw new IllegalArgumentException("Cannot encode another type of instruction other than the 4 primitives.");
        }
    }

    public int encodeVariable(Instruction inst){
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

    public void encode(Instruction inst){

    }
}
