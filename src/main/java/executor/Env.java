package executor;

public final class Env {
    private String id;
    private Value v;
    private Env next;

    public Env(String id, Value v, Env next) {
        this.id = id;
        this.v = v;
        this.next = next;
    }

    public void setNext(Env env) {
        this.next = env;

    }

    public String id() {
        return id;
    }

    public Value v() {
        return v;
    }

    public Env next() {
        return next;
    }


}
