package util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prime {
    public static boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> primeNumbersTill(int n) {
        return IntStream.rangeClosed(2, n)
                .filter(Prime::isPrime).boxed()
                .collect(Collectors.toList());
    }

    public static List<BigInteger> primeNumbersTill(BigInteger n) {
        List<BigInteger> res = new ArrayList<>();

        for(BigInteger i = BigInteger.valueOf(2); i.compareTo(n) < 0 || i.equals(n); i = i.add(BigInteger.ONE)){
            if(i.isProbablePrime(1000)) res.add(i);
        }

        return res;
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

    public static Map<BigInteger, Integer> primeToPow(BigInteger number) {
        BigInteger n = number;
        TreeMap<BigInteger, Integer> primeToPow = new TreeMap<>();
        for (BigInteger i = BigInteger.valueOf(2); i.compareTo(n) < 0 || i.compareTo(n) == 0; i = i.add(BigInteger.ONE)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                primeToPow.put(i, primeToPow.getOrDefault(i, 0)+1);
                n = n.divide(i);
            }
        }
        return primeToPow;
    }
}
