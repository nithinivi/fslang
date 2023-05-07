package ast;

public class LambdaExp extends Expression {
    private final Expression fParam;
    private final Expression body;

    public LambdaExp(Expression fParam, Expression body) {
        this.fParam = fParam;
        this.body = body;
    }

    public Expression fParam() {
        return fParam;
    }

    public Expression body() {
        return body;
    }
}
