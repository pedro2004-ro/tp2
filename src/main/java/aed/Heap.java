package aed;

import java.util.ArrayList;

public class Heap<T> {
    private ArrayList<Handler<T>> data;
    private Comparador<Handler<T>> prioridad;
    private int tamaño;

    public Heap(T[] datosIniciales, Comparador<Handler<T>> c, int[] ordenes) {
        data = new ArrayList<Handler<T>>();                //O(1)

        for (int i = 0; i < datosIniciales.length; i++) {            //O(T)
            data.add(new Handler<T>(datosIniciales[i], i));
            ordenes[i] = i;
        }
        
        tamaño = datosIniciales.length;                              //O(1)
        int i = dosALa(altura(tamaño)) - 2;                          //O(1)

        prioridad = c;                                          //O(1)

        array2Heap(i, ordenes);                                 //O(T)
    }

    public int registrar(Handler<T> nuevo) {               //O(log(T))
        int i = tamaño;                                         //O(1)

        if (tamaño == data.size())
            data.add(nuevo);
        else
            data.set(tamaño, nuevo);

        while (i > 0 && prioridad.comparar(data.get(padre(i)), data.get(i)) < 0) {  //log(T) iteraciones pq recorre la altura
            Handler<T> p = data.set(padre(i), nuevo);
            data.set(i, p);
            i = padre(i);
        }

        tamaño++;

        return i;
    }

    public Despachado<T> eliminar(int indice) {
        Handler<T> despachado = data.set(indice, data.get(tamaño - 1));    //O(1)

        data.set(tamaño - 1, null);                                     //O(1)
        tamaño--;                                                               //O(1)

        int i = indice;                                                         //O(1)

        i = siftDown(i, null);                                          

        Despachado<T> dataDespachado = new Despachado<T>(despachado, i);

        return dataDespachado;
    }

    public Handler<T> tope() {
        return data.get(0);
    }

    public void revisarPosicion(int indice, int[] ordenes) {
        indice = siftDown(indice, ordenes);                                                         //O(log(C))
        while (indice > 0 && prioridad.comparar(data.get(padre(indice)), data.get(indice)) < 0) {   //O(log(C))
            Handler<T> p = data.set(padre(indice), data.get(indice));
            data.set(indice, p);
            ordenes[p.handle()] = indice;
            indice = padre(indice);
            ordenes[data.get(indice).handle()] = indice;
        }
    }

    public ArrayList<Handler<T>> data() {
        return data;
    }

    private void array2Heap(int i, int[] ordenes) {

        while (i >= 0) {
            siftDown(i, ordenes);           //O(log(T))

            i--;                            //O(T/2)  (Por el algoritmo de Floyd, termina siendo O(T))
        }
    }

    private int siftDown(int i, int[] ordenes) {                    //log(T)
        while (i < tamaño && !hijosMenores(i)) {                    //log(T) iteraciones porque recorre la altura
            Handler<T> swap;                                        //O(1)
            if (hijoDer(i) >= tamaño) {
                swap = data.set(i, data.get(hijoIzq(i)));
                if (ordenes != null) {
                    ordenes[data.get(i).handle()] = i;
                    ordenes[swap.handle()] = hijoIzq(i);
                }
                i = hijoIzq(i);
            }
            else {
                Handler<T> hijoMaximo = max(data.get(hijoIzq(i)), data.get(hijoDer(i)));
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

    private int altura(int n) {
        int log = 0;
        int i = n;

        while (i > 1) {
            i /= 2;
            log++;
        }

        if (n > dosALa(log))
            return log + 1;
        else
            return log;
    }

    private int dosALa(int n) {
        int res = 1;
        while (n > 0) {
            res *= 2;
            n--;
        }

        return res;
    }

    /* 

    @Override
    public String toString() {
        String res = "";
        int maxFilas = log2Ceil(tamaño);
        int fila = 0;
        int tamañoFila = tamaño / 2;

        while (fila < maxFilas) {
            String filaOut = generarFila(fila, tamañoFila);
        }

        return res;
    }

    private String generarFila(int fila, int tamañoFila) {
        double espacios = tamañoFila - Math.pow(2, fila + 1);
    }

    private int log2Ceil(int n) {
        int log = 0;
        int i = n;

        while (i > 1) {
            i /= 2;
            log++;
        }

        if (n > Math.pow(2, log))
            return log + 1;
        else
            return log;
    }

    private double potenciaDeDosCeil(int n) {
        while (n > 0)
        return Math.pow(2, log2Ceil(n));
    }

    */

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

    private Handler<T> max(Handler<T> t, Handler<T> s) {
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
