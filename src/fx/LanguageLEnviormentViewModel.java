package fx;

public class LanguageLEnviormentViewModel {

    private LanguageLEnvironment env;

    public LanguageLEnviormentViewModel(LanguageLEnvironment env){
        this.env = env;
    }

    public String getCurrentLabel(){
        return env.getCurrentLabel();
    }

    public String getNextInstruction(){
        return  env.hasInstructions() ? String.valueOf(env.getNextInstruction().originalLine()) : "HALTED";
    }

    public String getPreviousInstruction(){
        return env.getPrevInstruction() != null ?
                String.valueOf(env.getPrevInstruction().originalLine()) : "No Previous Instruction";
    }

    public String getVariables(){
        return env.variables()
                .toString()
                .replace("=", " = ");
    }

    public String getSnapshot(){
        return (getI() + " ,   " + getVariables());
    }

    public String getI(){
        if(env.getNextInstruction() == null) return "i: " + (env.getInstructionCount() + 1);
        return "i: " + (env.getNextInstruction().lineNumber());
    }

    public String getExecutionCount(){
        return "Execution: #" + env.getExecutionCount() + " to " + env.getCurrentLabel();
    }
}
