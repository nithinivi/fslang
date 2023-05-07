package ast;

public class BoolCon extends  Expression{
    private final Boolean boolValue;

    public BoolCon(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public Boolean boolValue() {
        return boolValue;
    }
}
