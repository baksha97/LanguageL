package language.memory;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class VariableMemory {

    private final Map<String, Integer> vars;

    public VariableMemory() {
        vars = new TreeMap<>(new VariableComparator());
    }

    public void put(String var, int val) {
        if (val < 0) throw new IllegalArgumentException("You cannot put negative variables into the state.");
        vars.put(var, val);
    }

    public void initIfAbsent(String var) {
        if (!vars.containsKey(var)) put(var, 0);
    }

    public void incrementVariable(String var) {
        put(var, get(var) + 1);
    }

    public void decrementVariable(String var) {
        if (get(var) <= 0) return;
        int value = get(var) - 1;
        put(var, value);
    }

    public void replaceWith(String to, String from) {
        put(to, get(from));
    }

    public void reset(String var) {
        put(var, 0);
    }

    public boolean isNotZero(String var) {
        return get(var) != 0;
    }

    private int get(String var) {
        return vars.getOrDefault(var, 0);
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
