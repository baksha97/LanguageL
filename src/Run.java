import language.parse.Instruction;
import language.parse.InstructionType;
import language.parse.numeric.Decoder;
import language.parse.numeric.Encoder;

public class Run {

    public static void main(String... args) {
        Encoder encoder = new Encoder();
        Instruction test = new Instruction(InstructionType.INCREMENT,("X4" + " <- " + "X4" + " + 1"), "A4", "<>");
        System.out.println(encoder.encodeVariable(test));
//        System.out.println((int) 'A');
//        System.out.println((int) 'E');
//        System.out.println((char) 'A' - 5);
//        System.out.println((char) ('E' + 5));
//        System.out.println(('F' - 'C'));
//        InstructionFactory f = new InstructionFactory();
//        DecodedProgram d = new DecodedProgram(199);
//        System.out.println(d);
//        d = new DecodedProgram(3,0,2);
//        System.out.println(d.getDecodedCode());
    }


}
