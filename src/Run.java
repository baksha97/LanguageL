import language.parse.InstructionFactory;
import language.parse.ProgramDecoder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Run {

    public static void main(String... args) {
        InstructionFactory f = new InstructionFactory();
//        System.out.println(f.decodeInstruction(0));

        ProgramDecoder d = new ProgramDecoder(199);
        System.out.println(d);
//        System.out.println(primeNumbersTill(5));
//        System.out.println(primeListOf(5));
//        System.out.println(primeToPow(200));
    }

    public static Map<Integer, Integer> primeToPow(int number) {
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

    private static boolean isPrime(int number) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .filter(n -> (n & 0X1) != 0)
                .allMatch(n -> number % n != 0);
    }

    public static int kthPrime(int k) {
        int candidate, count;
        for(candidate = 2, count = 0; count < k; ++candidate) {
            if (isPrime(candidate)) {
                ++count;
            }
        }
        // The candidate has been incremented once after the count reached n
        return candidate-1;
    }

    public static List<Integer> primeListOf(int k){
        return primeNumbersTill(kthPrime(k));
    }

    public static List<Integer> primeNumbersTill(int n) {
        return IntStream.rangeClosed(2, n)
                .filter(Run::isPrime).boxed()
                .collect(Collectors.toList());
    }

//    public static

//
//    public static void primeFactors(int n)
//    {
//        // Print the number of 2s that divide n
//        while (n%2==0)
//        {
//            System.out.print(2 + " ");
//            n /= 2;
//        }
//
//        // n must be odd at this point.  So we can
//        // skip one element (Note i = i +2)
//        for (int i = 3; i <= Math.sqrt(n); i+= 2)
//        {
//            // While i divides n, print i and divide n
//            while (n%i == 0)
//            {
//                System.out.print(i + " ");
//                n /= i;
//            }
//        }
//
//        // This condition is to handle the case whien
//        // n is a prime number greater than 2
//        if (n > 2)
//            System.out.print(n);
//    }


//    public static class Decoder{
//
//        public GodelNumber decode(int z){
//            z = z + 1; // 2^x * (2y + 1) - 1 = z
//            //add 1 to both sides.
//            int x = 0;
//            while (z % 2 == 0){
//                z = z /2;
//                x++;
//                System.out.println(2 + " - " + z);
//            }
//
//            System.out.println();
//            System.out.println(x);
//            //found x, now we need to find y... now that
//            //2y + 1 = z
//            // subtract 1 from both size, then divide by 2.
//            z = z - 1;
//            int y = z / 2;
//            System.out.println(y);
//
//            return new GodelNumber(x, y);
//        }
//
//    }

//    public class Godel{
//        public class Decoder{
//
//        }
//    }

}
