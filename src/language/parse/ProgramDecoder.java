package language.parse;
import language.memory.LanguageRuntime;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProgramDecoder {
    private Map<Integer, Integer> primeToPow;
    int p;
    InstructionFactory factory;
    LanguageRuntime rt;

    public ProgramDecoder(int p){
        this.p = p;
        this.factory = new InstructionFactory();
        this.rt = new LanguageRuntime();
        this.primeToPow = primeToPow(p + 1);

        verifyNoMissingPrimes();

        primeToPow.forEach((prime,power) -> {
            Instruction instruction = factory.decodeInstruction(power);
            rt.addInstruction(instruction.getLabel(), instruction);
        });

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
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .filter(n -> (n & 0X1) != 0)
                .allMatch(n -> number % n != 0);
    }

    public List<Integer> primeNumbersTill(int n) {
        return IntStream.rangeClosed(2, n)
                .filter(this::isPrime).boxed()
                .collect(Collectors.toList());
    }

    private Map<Integer, Integer> primeToPow(int number) {
        int n = number;
        TreeMap<Integer, Integer> primeToPow = new TreeMap<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                System.out.println(i);
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
