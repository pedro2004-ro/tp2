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

        //arreglar sift up? (que se encargue de los handles)

        i = siftDown(i, null);

        DataDespachado dataDespachado = new DataDespachado(despachado, i);

        return dataDespachado;
    }

    private void array2Heap(int i, int[] ordenes) {

        while (i >= 0) {
            // hacer sift down con while
            siftDown(i, ordenes);

            i--;
        }
    }

    private int siftDown(int i, int[] ordenes) {
        while (i < tamaño && !hijosMenores(i)) {
            TrasladoHandles swap;
            if (hijoDer(i) >= tamaño) {
                swap = data.set(i, data.get(hijoIzq(i)));
                if (ordenes != null)
                    ordenes[swap.handle] = hijoIzq(i);
                i = hijoIzq(i);
            }
            else {
                TrasladoHandles hijoMaximo = max(data.get(hijoIzq(i)), data.get(hijoDer(i)));
                if (ordenes != null)
                    ordenes[hijoMaximo.handle] = i;
                swap = data.set(i, hijoMaximo);

                if (prioridad.comparar(data.get(hijoIzq(i)), data.get(hijoDer(i))) > 0) {
                    if (ordenes != null)
                        ordenes[swap.handle] = hijoIzq(i);
                    i = hijoIzq(i);
                }
                else {
                    if (ordenes != null)
                        ordenes[swap.handle] = hijoDer(i);
                    i = hijoDer(i);
                }

                data.set(i, swap);
            }
        }

        return i;
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
