class Variable {

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    private String name;
    private int value;

    public Variable(String name, int value){
        this.name = name;
        this.value = value;
    }

    public Variable(String name){
        this.name = name;
        this.value = 0;
    }

    public boolean notEqualsZero(){
        return value != 0;
    }

    public void decrement(){
        value--;
        if(value < 0) throw new IllegalArgumentException(getName() + ": " + getValue() + " has gone negative!");
    }

    public void increment(){
        value++;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public void copyContents(Variable v){
        this.value = v.getValue();
    }

    public void setZero() {
        value = 0;
    }
}
