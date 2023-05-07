package ast;

public class DecIn extends Expression {
    private final String name;
    private final Expression val;

    public DecIn(String name, Expression val) {
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
