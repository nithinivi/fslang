package ast;

public class CharCon extends  Expression{
    private final Character ch;

    public CharCon(Character ch) {
        this.ch = ch;
    }

    public Character ch() {
        return ch;
    }
}
