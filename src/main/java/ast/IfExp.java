package ast;

public class IfExp extends Expression{
    private final Expression expression1;
    private final Expression expression2;
    private final Expression expression3;


    public IfExp(Expression expression1, Expression expression2, Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    public Expression expression1() {
        return expression1;
    }

    public Expression expression2() {
        return expression2;
    }

    public Expression expression3() {
        return expression3;
    }
}
