package ast;

public class Decln extends Expression {
    private final String name;
    private final Expression val;

    public Decln(String name, Expression val) {
        this.name = name;
        this.val = val;
    }

    public Expression val() {
        return val;
    }

    public String name() {
        return name;
    }
}
