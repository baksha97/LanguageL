package util;

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
}
