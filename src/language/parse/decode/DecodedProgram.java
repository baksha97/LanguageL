package language.parse.decode;
import language.memory.LanguageRuntime;
import language.parse.Instruction;
import language.parse.InstructionFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecodedProgram {
    private Map<Integer, Integer> primeToPow;
    private InstructionFactory factory;
    private LanguageRuntime rt;

    private DecodedProgram(){
        this.factory = new InstructionFactory();
        this.rt = new LanguageRuntime();
    }

    public DecodedProgram(int p){
        this();
        this.primeToPow = primeToPow(p + 1);
        verifyNoMissingPrimes();
        for (Map.Entry<Integer, Integer> entry : primeToPow.entrySet()) {
            Integer power = entry.getValue();
            Instruction instruction = factory.decodeInstruction(power);

            if (instruction.getLabel() != null)
                rt.addInstruction(instruction.getLabel(), instruction);
            else
                rt.addInstructionToLastLabel(instruction);
        }
    }

    public DecodedProgram(int ... instructionNumbers){
        this();
        for (int instructionNumber : instructionNumbers) {
            Instruction instruction = factory.decodeInstruction(instructionNumber);

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
        List<Integer> primeNumbersTillMax = primeNumbersTill(maxPrime);
        primeNumbersTillMax.forEach(prime ->{
            if(!primeToPow.containsKey(prime)){
                primeToPow.put(prime, 0);
            }
        });

    }

    private boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> primeNumbersTill(int n) {
        return IntStream.rangeClosed(2, n)
                .filter(this::isPrime).boxed()
                .collect(Collectors.toList());
    }

    private Map<Integer, Integer> primeToPow(int number) {
        int n = number;
        TreeMap<Integer, Integer> primeToPow = new TreeMap<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                primeToPow.put(i, primeToPow.getOrDefault(i, 0)+1);
                n /= i;
            }
        }
        return primeToPow;
    }

    @Override
    public String toString() {
        return this.rt.toString();
    }
}
