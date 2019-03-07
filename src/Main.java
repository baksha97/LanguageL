import java.util.*;

public class Main {

    public static void main(String[] args){
        HashMap<String, Variable> varMap = new HashMap<>();
        Variable x = new Variable("X");
        Variable y = new Variable("Y");
        Variable z = new Variable("Z");

        varMap.put(x.getName(), x);
        varMap.put(y.getName(), y);
        varMap.put(z.getName(), z);

        HashMap<String, StateBlock> stateMap = new HashMap<>();
        List<Instruction> ains = Arrays.asList(
                new Instruction("X <- X + 1"),
                new Instruction("X <- X + 1"),
                new Instruction("X <- X - 1"),
                new Instruction("IF Z != 0 GOTO b"));
        StateBlock a = new StateBlock("a", ains);


        List<Instruction> bins = Arrays.asList(
                new Instruction("X <- X + 1"),
                new Instruction("X <- X + 1"),
                new Instruction("X <- X + 1"),
                new Instruction("X <- X + 1"));
        StateBlock b = new StateBlock("b", bins);


        stateMap.put(a.getName(),a);
        stateMap.put(b.getName(), b);

        StateBlock start = a;

        while(start.hasNextInstruction()){
            System.out.println(varMap);
            StateBlock newState = a.executeNext(varMap,stateMap);
            System.out.println(newState);
            if(newState != null){
                start = newState;
            }
        }


//        Queue<InstructionRunnable> instructions = new LinkedList<>();
//
//        instructions.offer(() -> varMap.get("x").increment());
//        instructions.offer(() -> varMap.get("x").increment());
//        instructions.offer(() -> varMap.get("x").increment());
//
//        System.out.println(varMap);
//        instructions.poll().execute();
//        System.out.println(varMap);
//        instructions.poll().execute();
//        System.out.println(varMap);
//        instructions.poll().execute();
//        System.out.println(varMap);
//        new Instruction("se"){
//            @Override
//            public void execute(){
//
//            }
//        };
    }


}
