import language.LanguageLEnvironment;
import org.junit.Assert;
import org.junit.Test;


public class LanguageEnvironmentTest {

    @Test
    public void x1Cubed_3x2_test1() {
        String input = "X1=3, X2=2";
        makeTest(input, x1Cubed_Threex2(), 33);
    }

    @Test
    public void x1Cubed_3x2_test2() {
        String input = "X1=0, X2=5";
        makeTest(input, x1Cubed_Threex2(), 15);
    }

    @Test
    public void x1Cubed_3x2_test3() {
        String input = "X1=4, X2=0";
        makeTest(input, x1Cubed_Threex2(), 64);
    }
//
    @Test
    public void x1_ToPower_x2_test1() {
        String input = "X1=0, X2=8";
        makeTest(input, x1_ToPower_x2(), 0);
    }

    @Test
    public void x1_ToPower_x2_test2() {
        String input = "X1=0, X2=0";
        makeTest(input, x1_ToPower_x2(), 1);
    }

    @Test
    public void x1_ToPower_x2_test3() {
        String input = "X1=1, X2=5";
        makeTest(input, x1_ToPower_x2(), 1);
    }

    @Test
    public void x1_ToPower_x2_test4() {
        String input = "X1=2, X2=10";
        makeTest(input, x1_ToPower_x2(), 1024);
    }

    @Test
    public void x1_ToPower_x2_test5() {
        String input = "X1=2, X2=20";
        makeTest(input, x1_ToPower_x2(), 1048576);
    }

    @Test
    public void xCubed_test1() {
        String input = "X1=10";
        makeTest(input, xCubed(), 1000);
    }

    @Test
    public void xCubed_test2() {
        String input = "X1=0";
        makeTest(input, xCubed(), 0);
    }

    @Test
    public void xCubed_test3() {
        String input = "X1=1";
        makeTest(input, xCubed(), 1);
    }

    private void makeTest(String input, String program, int expectedY){
        LanguageLEnvironment env = new LanguageLEnvironment(program, input);
        env.stopKeepingHistory();
        while(env.hasInstructions()) env.executeNext();
        Assert.assertEquals(expectedY, env.variables().get("Y"));
        Assert.assertEquals("E", env.getCurrentLabel());
        Assert.assertNull(env.getNextInstruction());
    }

    private String xCubed(){
        String program = "Z1 <- X1\n" +
                "Z4 <- Z4 + 1\n" +
                "\n" +
                "[C]\n" +
                "IF Z1 != 0 GOTO A\n" +
                "IF Z4 != 0 GOTO B\n" +
                "GOTO E\n" +
                "\n" +
                "[A]\n" +
                "Z3 <- X1\n" +
                "\n" +
                "[D]\n" +
                "Y <- Y + 1\n" +
                "Z3 <- Z3 - 1\n" +
                "IF Z3 != 0 GOTO D\n" +
                "Z1 <- Z1 - 1\n" +
                "GOTO C\n" +
                "\n" +
                "[B]\n" +
                "Z1 <- Y\n" +
                "Y <- 0\n" +
                "Z4 <- Z4 - 1\n" +
                "GOTO C";
        return program;
    }

    private String x1Cubed_Threex2(){
        String program =
                "IF X1 != 0 GOTO A2\n" +
                        "Y <- Y\n" +
                        "GOTO D\n" +
                        "\n" +
                        "[A2]\n" +
                        "Z4 <- Z4 + 1\n" +
                        "Z4 <- Z4 + 1\n" +
                        "Z4 <- Z4 + 1\n" +
                        "\n" +
                        "[A]\n" +
                        "Z1 <- X1\n" +
                        "Z2 <- Y\n" +
                        "Y <- 0\n" +
                        "\n" +
                        "[B]\n" +
                        "Z1 <- Z1 - 1\n" +
                        "Z3 <- Z2\n" +
                        "\n" +
                        "[C]\n" +
                        "Y <- Y + 1\n" +
                        "Z3 <- Z3 - 1\n" +
                        "IF Z3 != 0 GOTO C\n" +
                        "IF Z1 != 0 GOTO B\n" +
                        "Z4 <- Z4 - 1\n" +
                        "IF Z4 != 0 GOTO A\n" +
                        "\n" +
                        "[D]\n" +
                        "\n" +
                        "IF X2 != 0 GOTO D2\n" +
                        "GOTO E\n" +
                        "[D2]\n" +
                        "X2 <- X2 - 1\n" +
                        "Y <- Y + 1\n" +
                        "Y <- Y + 1\n" +
                        "Y <- Y + 1\n" +
                        "GOTO D";
        return program;
    }

    private String x1_ToPower_x2(){
        String program = "IF X2 != 0 GOTO D\n" +
                "Y <- Y + 1\n" +
                "GOTO E\n" +
                "\n" +
                "[D]\n" +
                "IF X1 != 0 GOTO A\n" +
                "Y <- 0\n" +
                "GOTO E\n" +
                "\n" +
                "[A]\n" +
                "Z1 <- X1\n" +
                "Z2 <- Y\n" +
                "Y <- 0\n" +
                "\n" +
                "[B]\n" +
                "Z1 <- Z1 - 1\n" +
                "Z3 <- Z2\n" +
                "\n" +
                "[C]\n" +
                "Y <- Y + 1\n" +
                "Z3 <- Z3 - 1\n" +
                "IF Z3 != 0 GOTO C\n" +
                "IF Z1 != 0 GOTO B\n" +
                "X2 <- X2 - 1\n" +
                "IF X2 != 0 GOTO A\n" +
                "GOTO E";
        return program;
    }
}
