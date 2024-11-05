package aed;

import java.util.ArrayList;

public class Heap<T> {
    public ArrayList<TrasladoHandles> data;
    private Comparador<TrasladoHandles> prioridad;
    public int tamaño;

    public Heap(Traslado[] traslados, Comparador<TrasladoHandles> c, int[] ordenes) {
        data = new ArrayList<TrasladoHandles>();

        for (int i = 0; i < traslados.length; i++) {
            data.add(new TrasladoHandles(traslados[i], i));
            ordenes[i] = i;
        }
        
        tamaño = traslados.length;
        int i = padre(tamaño - 1);

        prioridad = c;

        array2Heap(i, ordenes);

    }

    public int registrar(TrasladoHandles nuevo) {
        int i = data.size();
        
        data.add(nuevo);

        while (i > 0 && prioridad.comparar(data.get(padre(i)), data.get(i)) < 0) {
            TrasladoHandles p = data.set(padre(i), nuevo);
            data.set(i, p);
            i = padre(i);
        }

        tamaño++;

        return i;
    }

    public DataDespachado eliminar(int indice) {
        TrasladoHandles despachado = data.set(indice, data.get(tamaño - 1));

        data.set(tamaño - 1, null);
        tamaño--;

        int i = indice;            

        while (i < tamaño && !hijosMenores(i)) {
            TrasladoHandles t;

            if (hijoDer(i) >= tamaño) {
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

        DataDespachado dataDespachado = new DataDespachado(despachado.traslado.id, despachado.handle, i);

        return dataDespachado;
    }

    private void array2Heap(int i, int[] ordenes) {

        while (i >= 0) {
            if (!hijosMenores(i)) {
                TrasladoHandles hijoMaximo = max(data.get(hijoIzq(i)), data.get(hijoDer(i)));
                ordenes[hijoMaximo.handle] = i;
                TrasladoHandles swap = data.set(i, hijoMaximo);

                if (prioridad.comparar(data.get(hijoIzq(i)), data.get(hijoDer(i))) > 0) {
                    ordenes[swap.handle] = hijoIzq(i);
                    swap.handle = hijoIzq(i); //MAL!
                    data.set(hijoIzq(i), swap);
                }
                else {
                    ordenes[swap.handle] = hijoDer(i);
                    swap.handle = hijoDer(i); //MAL!
                    data.set(hijoDer(i), swap);
                }
            }

            i--;
        }
    }

    private int hijoIzq(int i) {
        return 2*i + 1;
    }

    private boolean hijosMenores(int i) {
        return (hijoDer(i) >= tamaño || prioridad.comparar(data.get(i), data.get(hijoDer(i))) >= 0) && 
        (hijoIzq(i) >= tamaño || prioridad.comparar(data.get(i), data.get(hijoIzq(i))) >= 0);
    }

    private TrasladoHandles max(TrasladoHandles t, TrasladoHandles s) {
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
