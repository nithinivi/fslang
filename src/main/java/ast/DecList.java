package ast;

public class DecList extends Expression {
    private final Boolean recursive;
    private final Expression hd;
    private final Expression tl;

    public DecList(Boolean recursive, Expression hd, Expression tl) {
        this.recursive = recursive;
        this.hd = hd;
        this.tl = tl;
    }

    public Expression hd() {
        return hd;
    }

    public Expression tl() {
        return tl;
    }

    public Boolean recursive() {
        return recursive;
    }
}
