package aed;

class ComparadorPorRedito<T> implements Comparador<TrasladoHandles> {
    @Override
    public int comparar(TrasladoHandles a, TrasladoHandles b) {
        if (a.traslado.gananciaNeta > b.traslado.gananciaNeta)
            return 1;
        else if (a.traslado.gananciaNeta == b.traslado.gananciaNeta) {
            if (a.traslado.id < b.traslado.id) 
                return 1;
            else 
                return -1;
        }
        else 
            return -1;
    }
}

class ComparadorPorAntiguedad<T> implements Comparador<TrasladoHandles> {
    @Override
    public int comparar(TrasladoHandles a, TrasladoHandles b) {
        if (a.traslado.timestamp < b.traslado.timestamp) {
            return 1;
        }
        else
            return -1;
    }
}
