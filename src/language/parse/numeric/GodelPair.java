package language.parse.numeric;

import java.math.BigInteger;

public class GodelPair {
    private final BigInteger x;
    private final BigInteger y;
    private final BigInteger z;

    public GodelPair(long x, long y) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = findZ(this.x, this.y);
    }

    public GodelPair(long z) {
        this.z = BigInteger.valueOf(z);
        BigInteger[] tupleOfXY = findXY(this.z);

        this.x = tupleOfXY[0];
        this.y = tupleOfXY[1];
    }

    public GodelPair(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
        this.z = findZ(x, y);
    }

    public GodelPair(BigInteger z) {
        this.z = z;
        BigInteger[] tupleOfXY = findXY(z);

        this.x = tupleOfXY[0];
        this.y = tupleOfXY[1];
    }

    private BigInteger findZ(BigInteger x, BigInteger y) {
        BigInteger leftHandSize = BigInteger.valueOf(2).pow(x.intValue())
                .multiply(
                        BigInteger.valueOf(2).multiply(y)
                                .add(BigInteger.ONE)
                );// 2^x * (2y + 1)
        leftHandSize = leftHandSize.subtract(BigInteger.ONE); // 2^x * (2y + 1) - 1 = z
        return leftHandSize;
    }

    private BigInteger[] findXY(BigInteger z) {
        z = z.add(BigInteger.ONE); // 2^x * (2y + 1) - 1 = z
        //add 1 to both sides.
        BigInteger x = BigInteger.ZERO;
        while (z.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            z = z.divide(BigInteger.valueOf(2));
            x = x.add(BigInteger.ONE);
        }

        //found x, now we need to find y... now that
        //2y + 1 = z
        // subtract 1 from both size, then divide by 2.
        z = z.subtract(BigInteger.ONE);
        BigInteger y = z.divide(BigInteger.valueOf(2));

        return new BigInteger[]{x, y};
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public BigInteger getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "GodelPair{<" + x + ", " + y + "> = " + z + '}';
    }
}
