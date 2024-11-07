package aed;

import java.util.ArrayList;

public class Heap<T> {
    public ArrayList<TrasladoHandles> data;
    private Comparador<TrasladoHandles> prioridad;
    private int tamaño;

    public Heap(Traslado[] traslados, Comparador<TrasladoHandles> c, int[] ordenes) {
        data = new ArrayList<TrasladoHandles>();                //O(1)

        for (int i = 0; i < traslados.length; i++) {            //O(T)
            data.add(new TrasladoHandles(traslados[i], i));
            ordenes[i] = i;
        }
        
        tamaño = traslados.length;                              //O(1)
        int i = padre(tamaño - 1);                              //O(1)

        prioridad = c;                                          //O(1)

        array2Heap(i, ordenes);                                 //O(T)
    }

    public int registrar(TrasladoHandles nuevo) {               //O(log(T))
        int i = tamaño;                                         //O(1)

        if (tamaño == data.size())
            data.add(nuevo);
        else
            data.set(tamaño, nuevo);

        while (i > 0 && prioridad.comparar(data.get(padre(i)), data.get(i)) < 0) {  //log(T) iteraciones pq recorre la altura
            TrasladoHandles p = data.set(padre(i), nuevo);
            data.set(i, p);
            i = padre(i);
        }

        tamaño++;

        return i;
    }

    public DataDespachado eliminar(int indice) {
        TrasladoHandles despachado = data.set(indice, data.get(tamaño - 1));    //O(1)

        data.set(tamaño - 1, null);                                     //O(1)
        tamaño--;                                                               //O(1)

        int i = indice;                                                         //O(1)

        i = siftDown(i, null);                                          

        DataDespachado dataDespachado = new DataDespachado(despachado, i);

        return dataDespachado;
    }

    private void array2Heap(int i, int[] ordenes) {

        while (i >= 0) {
            siftDown(i, ordenes);           //O(log(T))

            i--;                            //O(T/2)  (Por el algoritmo de Floyd, termina siendo O(T))
        }
    }

    private int siftDown(int i, int[] ordenes) {                    //log(T)
        while (i < tamaño && !hijosMenores(i)) {                    //log(T) iteraciones porque recorre la altura
            TrasladoHandles swap;                                   //O(1)
            if (hijoDer(i) >= tamaño) {
                swap = data.set(i, data.get(hijoIzq(i)));
                if (ordenes != null)
                    ordenes[swap.handle()] = hijoIzq(i);
                i = hijoIzq(i);
            }
            else {
                TrasladoHandles hijoMaximo = max(data.get(hijoIzq(i)), data.get(hijoDer(i)));
                if (ordenes != null)
                    ordenes[hijoMaximo.handle()] = i;
                swap = data.set(i, hijoMaximo);

                if (prioridad.comparar(data.get(hijoIzq(i)), data.get(hijoDer(i))) > 0) {
                    if (ordenes != null)
                        ordenes[swap.handle()] = hijoIzq(i);
                    i = hijoIzq(i);
                }
                else {
                    if (ordenes != null)
                        ordenes[swap.handle()] = hijoDer(i);
                    i = hijoDer(i);
                }

            }
            data.set(i, swap);
        }

        return i;
    }

    public int tamaño() {
        return tamaño;
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
