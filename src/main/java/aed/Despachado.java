package aed;

public class Despachado<T> {
    private Handler<T> traslado;
    private int posHoja;

    public Despachado(Handler<T> t, int pos) {
        traslado = t;
        posHoja = pos;
    }

    public Handler<T> traslado() {
        return traslado;
    }

    public int posHoja() {
        return posHoja;
    }
}
