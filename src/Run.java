import fx.LanguageLEnvironment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Run {

    public static void main(String... args) throws FileNotFoundException {
        File file = new File("YminusZ.txt");
        Scanner in = new Scanner(file);
        LanguageLEnvironment env = new LanguageLEnvironment(in, "Y=5,Z=3");
//        System.out.println(env.variables());
        System.out.println("Starting...");

        while (env.hasInstructions()) {
            System.out.println(env.getExecutionCount());
            System.out.println(env.getCurrentState());
            System.out.println(env.variables());
            System.out.println(env.getNextInstruction());
            System.out.println();
            env.executeNext();
        }

        System.out.println("\n" +
                "\n\nTerminated....");
        System.out.println(env.variables());
        System.out.println(env.getCurrentState());
        System.out.println(env.getNextInstruction());
//
//
////        File file = new File("pro.txt");
////        File file = new File("p2.txt");
//        InstructionFactory factory = new InstructionFactory();
//        File file = new File("x1minusx2.txt");
//        Scanner in = new Scanner(file);
//
//        LinkedMap<String, List<Instructable>> states = new LinkedMap<>();
//        HashMap<String, Integer> vals = new HashMap<>();
//
//        //initialize blocks
//        while(in.hasNextLine()){
//            String line = in.nextLine();
//            if(line.isEmpty()) continue;
//            if(line.contains("[")){
//                states.put(line.replace("[","").replace("]",""), new ArrayList<>());
//            }else{
//                Instructable instruction = factory.getInstruction(line);
//                states.get(states.lastKey()).add(instruction);
//                if(instruction.getType() != InstructionType.GOTO_MACRO) vals.put(instruction.getVarName(), 0);
//            }
//        }
//
//
//        //execute blocks
//        System.out.println("init... \n");
////        vals.put("X", new Variable("X", 3));
//
//        ///x1-x2 input
//        vals.put("Y", 5);
//        vals.put("Z", 3);
//
//        System.out.println(states);
//        System.out.println(vals);
//
//        String currentState = states.firstKey();
//        List<Instructable> instructions = states.get(currentState);
//
//        System.out.println("\nExecuting...");
//
//        int r = 0;
//
//        while(instructions != null){
//            for(int i=0; i<instructions.size(); i++){
//                System.out.println(++r);
//                Instructable inst = instructions.get(i);
//
//                if(inst instanceof GoToMacro){
//                    String newStateName = ((GoToMacro) inst).getNewStateName();
//                    if(!states.containsKey(newStateName)){
//                        instructions = null;
//                        break;
//                    }
//                }
//
//                if(inst.getGoToMacroNewState(states) != null){
//                    instructions = inst.getGoToMacroNewState(states);
//                    break;
//                }
//
//                if(inst.nextState(states, vals)){
//                    instructions = inst.executeOn(states, vals);
//                    break;
//                }
//
//
//                inst.executeOn(states, vals);
//                System.out.println(vals);
//            }
//
//        }
//    }
    }
}
