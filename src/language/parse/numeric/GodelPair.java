package language.parse.numeric;

public class GodelPair {
    public final int x;
    public final int y;

    public GodelPair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public GodelPair(int z){
        z = z + 1; // 2^x * (2y + 1) - 1 = z
        //add 1 to both sides.
        int x = 0;
        while (z % 2 == 0){
            z = z /2;
            x++;
        }

        //found x, now we need to find y... now that
        //2y + 1 = z
        // subtract 1 from both size, then divide by 2.
        z = z - 1;
        int y = z / 2;

        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return "GodelPair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
