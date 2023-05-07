package ast;

public class NumCon extends  Expression{
    private final double number;

    public NumCon(double number) {
        this.number = number;
    }

    public double number() {
        return number;
    }
}
