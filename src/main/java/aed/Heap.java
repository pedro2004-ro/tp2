package aed;

import java.util.ArrayList;

public class Heap<T> {
    private ArrayList<TrasladoHandles> data;
    private Comparador<TrasladoHandles> prioridad;

    public Heap(Traslado[] traslados, Comparador<TrasladoHandles> c) {
        data = new ArrayList<TrasladoHandles>();
        for (int i = 0; i < traslados.length; i++) {
            data.add(traslados[i]);
        }

        prioridad = c;
    }

    public int registrar(Traslado traslado) {
        int i = data.size();
        
        data.add(traslado);

        while (i > 0 && prioridad.comparar(data.get(padre(i)), data.get(i)) < 0) {
            Traslado p = data.set(padre(i), traslado);
            data.set(i, p);
            i = padre(i);
        }

        return i;
    }

    public int despacharUno() {
        Traslado despachado = data.set(0, data.get(data.size()-1));

        data.remove(data.size()-1);

        int i = 0;

        while (!hijosMenores(data, i)) {
            Traslado t;

            if (hijoDer(i) >= data.size()) {
                t = data.set(i, data.get(hijoIzq(i)));
                i = hijoIzq(i);
            }
            else {
                t = data.set(i, max(data.get(hijoIzq(i)), data.get(hijoDer(i))));

                if (prioridad.comparar(data.get(hijoIzq(i)), data.get(hijoDer(i))) < 0)
                    i = hijoDer(i);
                else
                    i = hijoIzq(i);
            }

            data.set(i, t);
        }

        return despachado.id;
    }

    private int hijoIzq(int i) {
        return 2*i + 1;
    }

    private boolean hijosMenores(ArrayList<Traslado> data, int i) {
        return (hijoDer(i) >= data.size() || prioridad.comparar(data.get(i), data.get(hijoDer(i))) >= 0) && 
        (hijoIzq(i) >= data.size() || prioridad.comparar(data.get(i), data.get(hijoIzq(i))) >= 0);
    }

    private Traslado max(Traslado t, Traslado s) {
        if (prioridad.comparar(t, s) > 0)
            return t;
        else
            return s;
    }

    private int hijoDer(int i) {
        return 2*i + 2;
    }

    private int padre(int i) {
        return (i-1)/2;
    }
}
