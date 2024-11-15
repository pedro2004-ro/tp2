package aed;

public class Despachado<T> {
    private Handler<T> dato;
    private int posHoja;

    public Despachado(Handler<T> t, int pos) {
        dato = t;
        posHoja = pos;
    }

    public Handler<T> dato() {
        return dato;
    }

    public int posHoja() {
        return posHoja;
    }
}
