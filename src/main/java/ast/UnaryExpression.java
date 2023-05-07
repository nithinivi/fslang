package ast;

import lexer.TOKEN;

public class UnaryExpression extends Expression {
    private final Expression unary_expression;
    private final TOKEN unaryOperator;

    public UnaryExpression(Expression unaryExpression, TOKEN unaryOperator) {
        unary_expression = unaryExpression;
        this.unaryOperator = unaryOperator;
    }

    public Expression unaryExpression() {
        return unary_expression;
    }

    public TOKEN operator() {
        return unaryOperator;
    }
}
