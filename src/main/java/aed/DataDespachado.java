package aed;

public class DataDespachado {
    private TrasladoHandles traslado;
    private int posHoja;

    public DataDespachado(TrasladoHandles t, int pos) {
        traslado = t;
        posHoja = pos;
    }

    public TrasladoHandles traslado() {
        return traslado;
    }

    public int posHoja() {
        return posHoja;
    }
}
