package executor;

import ast.Expression;

public final class Value {
    ValueType type;
    private Value hd;
    private Value tl;
    private Env env;
    private Expression exp;
    private Character ch;
    private Double n;
    private Boolean b;

    public Value(ValueType valueType, Expression program, Env env) {
        this.type = valueType;
        this.exp = program;
        this.env = env;
    }

    public Value(double number) {
        this.n = number;
        this.type= ValueType.Number;
    }

    public Value(Boolean aBoolean) {
        this.b = aBoolean;
        this.type = ValueType.Boolean;
    }

    public Value(Character c) {
        this.ch = c;
        this.type = ValueType.Character;
    }

    public Value(ValueType valueType) {
        this.type = valueType;
    }

    public Value(Value hd, Value tl) {
        this.type = ValueType.List;
        this.hd = hd;
        this.tl = tl;
    }

    public ValueType type() {
        return type;
    }

    public Value getHd() {
        return hd;
    }

    public Value getTl() {
        return tl;
    }

    public Env getEnv() {
        return env;
    }

    public Expression getExp() {
        return exp;
    }

    public Character getCh() {
        return ch;
    }

    public Double number() {
        return n;
    }

    public Boolean boolValue() {
        return b;
    }


    public void setValue(Value that) {
        this.hd = that.hd;
        this.tl = that.tl;
        this.env = that.env;
        this.exp = that.exp;
        this.ch = that.ch;
        this.n = that.n;
        this.b = that.b;
        this.type = that.type;
    }
}
