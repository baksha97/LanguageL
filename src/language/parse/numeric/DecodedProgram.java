package language.parse.numeric;
import language.memory.LanguageRuntime;
import language.parse.Instruction;
import util.Prime;

import java.util.List;
import java.util.Map;

public class DecodedProgram {
    private Map<Integer, Integer> primeToPow;
    private Decoder decoder;
    private LanguageRuntime rt;

    private DecodedProgram(){
        this.decoder = new Decoder();
        this.rt = new LanguageRuntime();
    }

    public DecodedProgram(int p){
        this();
        this.primeToPow = Prime.primeToPow(p + 1);
        verifyNoMissingPrimes();
        for (Map.Entry<Integer, Integer> entry : primeToPow.entrySet()) {
            Integer power = entry.getValue();
            Instruction instruction = decoder.decodeInstruction(power);

            if (instruction.getLabel() != null)
                rt.addInstruction(instruction.getLabel(), instruction);
            else
                rt.addInstructionToLastLabel(instruction);
        }
    }

    public DecodedProgram(int ... instructionNumbers){
        this();
        for (int instructionNumber : instructionNumbers) {
            Instruction instruction = decoder.decodeInstruction(instructionNumber);

            if(instruction.getLabel() != null)
                rt.addInstruction(instruction.getLabel(), instruction);
            else
                rt.addInstructionToLastLabel(instruction);
        }
    }

    public String getDecodedCode(){
        StringBuilder sb = new StringBuilder();
        System.out.println(rt.getInstructionMap().entrySet());
        for(Map.Entry<String, List<Instruction>> entry: rt.getInstructionMap().entrySet()){
            if(!entry.getKey().equalsIgnoreCase(Instruction.DEFAULT_UNLABELED_LABEL)){
             sb.append("[").append(entry.getKey()).append("]\n");
            }
            List<Instruction> instructions = entry.getValue();
            instructions.forEach(instruction -> {
                sb.append("//").append(instruction.getGodelNotation()).append('\n');
                sb.append(instruction.getLine()).append("\n\n");
            });
        }
        return sb.toString();
    }

    private void verifyNoMissingPrimes(){
        int maxPrime = primeToPow.values().stream().mapToInt(i -> i).max().orElse(0);
        List<Integer> primeNumbersTillMax = Prime.primeNumbersTill(maxPrime);
        primeNumbersTillMax.forEach(prime ->{
            if(!primeToPow.containsKey(prime)){
                primeToPow.put(prime, 0);
            }
        });

    }



    @Override
    public String toString() {
        return this.rt.toString();
    }
}
