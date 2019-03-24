package language.memory;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class VariableMemory {

    private final Map<String, Integer> vars;

    public VariableMemory() {
        vars = new TreeMap<>(new VariableComparator());
    }

    public void init(String var) {
        if (!vars.containsKey(var)) put(var, 0);
    }

    public void put(String var, int val) {
        vars.put(var, val);
    }

    public void incrementVariable(String var) {
        int value = vars.getOrDefault(var, 0) + 1;
        vars.put(var, value);
    }

    public void decrementVariable(String var) {
        int value = vars.getOrDefault(var, 0) - 1;
        if (value < 0) return;
        vars.put(var, value);
    }

    public void copyVariableValue(String to, String from) {
        int value = vars.getOrDefault(from, 0);
        vars.put(to, value);
    }

    public void resetVariable(String var) {
        put(var, 0);
    }

    public boolean variableNotZero(String var) {
        return vars.getOrDefault(var, 0) != 0;
    }

    @Override
    public String toString() {
        return vars.toString();
    }

    class VariableComparator implements Comparator<String> {
        public int compare(String obj1, String obj2) {
            if (obj1.equalsIgnoreCase("Y") && !obj2.equalsIgnoreCase("Y")) {
                return 1;
            } else if (obj2.equalsIgnoreCase("Y") && !obj1.equalsIgnoreCase("Y")) {
                return -1;
            }
            return obj1.compareTo(obj2);
        }
    }
}
