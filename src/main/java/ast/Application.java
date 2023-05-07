package ast;

public class Application extends  Expression{
    private final Expression fun;
    private final Expression aParm;

    public Application(Expression fun, Expression aparm) {
        this.fun = fun;
        this.aParm = aparm;
    }

    public Expression fun() {
        return fun;
    }

    public Expression aparm() {
        return aParm;
    }
}
