package language;

public class LanguageLEnvironmentViewModel {

    private final LanguageLEnvironment env;

    public LanguageLEnvironmentViewModel(LanguageLEnvironment env) {
        this.env = env;
    }

    public String getCurrentLabel() {
        return env.getCurrentLabel();
    }

    public String getNextInstruction() {
        return env.hasInstructions() ? String.valueOf(env.getNextInstruction().getLine()) : "HALTED";
    }

    public String getPreviousInstruction() {
        return env.getPrevInstruction() != null ?
                String.valueOf(env.getPrevInstruction().getLine()) : "No Previous Instruction";
    }

    public String getVariables() {
        return env.variables()
                .toString()
                .replace(",", ",\t");
    }

    public String getSnapshot() {
        return (getI() + "\t,\t" + getVariables());
    }

    public String getI() {
        if (env.getNextInstruction() == null) return "i: " + (env.getInstructionCount() + 1);
        return "i: " + (env.getNextInstruction().getLineNumber());
    }

    public String getExecutionCount() {
        return "Execution: #" + env.getExecutionCount() + " : " + env.getCurrentLabel();
    }
}
