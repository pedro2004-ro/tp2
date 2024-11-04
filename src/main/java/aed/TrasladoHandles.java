package aed;

public class TrasladoHandles {
    private Traslado traslado;
    private int handle;

    public TrasladoHandles(Traslado t, int h) {
        traslado = t;
        handle = h;
    }

    public Traslado traslado() {
        return traslado;
    }

    public int handle() {
        return handle;
    }
}
