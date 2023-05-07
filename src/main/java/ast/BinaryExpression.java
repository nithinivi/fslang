package ast;

import lexer.TOKEN;

public class BinaryExpression extends Expression {
    private final TOKEN binaryOperator;
    private final Expression left_expr;
    private final Expression right_expr;

    public BinaryExpression(TOKEN binaryOperator, Expression left_expr, Expression right_expr) {
        this.binaryOperator = binaryOperator;
        this.right_expr = right_expr;
        this.left_expr = left_expr;
    }

    public Expression left() {
        return left_expr;
    }

    public Expression right() {
        return right_expr;
    }

    public TOKEN operator() {
        return binaryOperator;
    }
}
