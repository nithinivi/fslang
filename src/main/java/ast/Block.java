package ast;

public class Block extends Expression {
    private final Expression declarations;
    private final Expression expression;

    public Block(Expression declarations, Expression expression) {
        this.declarations = declarations;
        this.expression = expression;
    }

    public Expression declarations() {
        return declarations;
    }

    public Expression expression() {
        return expression;
    }
}

