package aed;

class ComparadorPorRedito<T> implements Comparador<TrasladoHandles> {
    @Override
    public int comparar(TrasladoHandles a, TrasladoHandles b) {
        if (a.gananciaNeta > b.gananciaNeta)
            return 1;
        else if (a.gananciaNeta == b.gananciaNeta) {
            if (a.id < b.id) 
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
        if (a.timestamp < b.timestamp) {
            return 1;
        }
        else
            return -1;
    }
}
