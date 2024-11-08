package aed;

class ComparadorPorRedito<T> implements Comparador<Handler<Traslado>> {
    @Override
    public int comparar(Handler<Traslado> a, Handler<Traslado> b) {
        if (a == null && b == null) {
            return 0;
        }
        else if (a == null) {
            return -1;
        }
        else if (b == null) {
            return 1;
        }
        else if (a.dato().gananciaNeta > b.dato().gananciaNeta)
            return 1;
        else if (a.dato().gananciaNeta == b.dato().gananciaNeta) {
            if (a.dato().id < b.dato().id) 
                return 1;
            else 
                return -1;
        }
        else 
            return -1;
    }
}

class ComparadorPorAntiguedad<T> implements Comparador<Handler<Traslado>> {
    @Override
    public int comparar(Handler<Traslado> a, Handler<Traslado> b) {
        if (a == null && b == null) {
            return -0;
        }
        else if (a == null) {
            return -1;
        }
        else if (b == null) {
            return 1;
        }
        else if (a.dato().timestamp < b.dato().timestamp) {
            return 1;
        }
        else
            return -1;
    }
}

class ComparadorPorSuperavit<T> implements Comparador<Handler<Ciudad>> {
    @Override
    public int comparar(Handler<Ciudad> a, Handler<Ciudad> b) {
        if (a.dato().superavit() > b.dato().superavit()) {
            return 1;
        }
        else if (a.dato().superavit() < b.dato().superavit()) {
            return -1;
        }
        else {
            if (a.handle() < b.handle()) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
}