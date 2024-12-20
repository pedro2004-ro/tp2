package aed;

import java.util.ArrayList;

public class Heap<T> {
    private ArrayList<Handler<T>> data;
    private Comparador<Handler<T>> prioridad;
    private int tamaño;

    // Para crear el heap, el constructor recibe de parámetros un array datosIniciales con los datos a almacenar. Además recibe un comparador para establecer la prioridad 
    //del heap (ya sea si es por mas redito, por mas antiguo o por mayor superavit). Por último, un array ordenes que nos va a servir en BestEffort para que, en caso de los traslados, 
    //los heaps esten siempre sincronizados y tenga los mismos elementos, y en el caso de las ciudades, para saber siempre en que posición del heap se encuentra cada una.


    public Heap(T[] datosIniciales, Comparador<Handler<T>> c, int[] ordenes) {   // O(n) donde n = |datosIniciales| = |ordenes|
        data = new ArrayList<Handler<T>>();                         //O(1)

        for (int i = 0; i < datosIniciales.length; i++) {           // n iteraciones, como lo de adentro del for es O(1), el costo es O(n)
            data.add(new Handler<T>(datosIniciales[i], i));         // O(1) el handle se inicializa con la posición original de cada dato en datosiniciales para poder actualizar ordenes
            ordenes[i] = i;                                         // O(1)
        }
        tamaño = datosIniciales.length;                             //O(1)
        int i = padre(tamaño - 1);                                  //O(1) se sitúa en la última posicion del penúltimo nivel del heap

        prioridad = c;                                              //O(1)

        array2Heap(i, ordenes);                                     //O(n)
    } 

    public int registrar(Handler<T> nuevo) {               //O(log(n))
        int i = tamaño;                                    //O(1)

        if (tamaño == data.size())                         //O(1)
            data.add(nuevo);
        else
            data.set(tamaño, nuevo);                      //O(1) 

        while (i > 0 && prioridad.comparar(data.get(padre(i)), data.get(i)) < 0) {  //log(n) iteraciones, ya que recorre la altura. EL costo es O(log n), ya que lo de adentro es O(1)
            Handler<T> p = data.set(padre(i), nuevo);                               //O(1) en la posición del padre de nuevo nodo asignamos a nuevo  
            data.set(i, p);                                                         //O(1) en la antigua posición de nuevo asignamos al padre
            i = padre(i);                                                           //O(1)
        }

        tamaño++;                                                                   //O(1)

        return i;                                                                   //O(1) nos devuelve la posicion donde queda el traslado
    }

    public Despachado<T> eliminar(int indice) {                            //O(log n)
        Handler<T> despachado = data.set(indice, data.get(tamaño - 1));    //O(1) setea en la posición del valor que queremos eliminar al último elemento del heap. Despachado es el valor original de esa posición.

        data.set(tamaño - 1, null);                                        //O(1) elimina el último elemento asignando null
        tamaño--;                                                          //O(1)

        int i = indice;                                                    //O(1)

        i = siftDown(i, null);                                     //O(log n) obtenemos en i la posicion donde quedo el ultimo elemento luego de desencolar 

        Despachado<T> dataDespachado = new Despachado<T>(despachado, i);   //O(1) la metemos en data despachado

        return dataDespachado;                                             //O(1) y por ultimo la devolvemos 
    }

    public Handler<T> tope() {   //O(1)
        return data.get(0);
    }

    //Se fija si hay que hacer siftDown o siftUp
    public void revisarPosicion(int indice, int[] ordenes) {                                    
        indice = siftDown(indice, ordenes);                                                         //O(log(n))
        while (indice > 0 && prioridad.comparar(data.get(padre(indice)), data.get(indice)) < 0) {   //O(log(n))
            Handler<T> p = data.set(padre(indice), data.get(indice));                   //O(1)
            data.set(indice, p);                                                        //O(1)
            ordenes[p.handle()] = indice;                                               //O(1)
            indice = padre(indice);                                                     //O(1)
            ordenes[data.get(indice).handle()] = indice;                                //O(1)
        }
    }

    public ArrayList<Handler<T>> data() {   //O(1)
        return data;
    }

    private void array2Heap(int i, int[] ordenes) { // O(n)

        while (i >= 0) {                    //n/2 iteraciones (Por el algoritmo de Floyd, el costo termina siendo O(n))
            siftDown(i, ordenes);           //O(log(n))

            i--;                         
        }
    } 

    private int siftDown(int i, int[] ordenes) {                   //O(log(n))
        while (i < tamaño && !hijosMenores(i)) {                   //log(n) iteraciones, ya que recorre la altura. Entonces, como lo de adentro es O(1), el costo del while es O(log n)
            Handler<T> swap;                                       //O(1)
            if (hijoDer(i) >= tamaño) {                            //O(1) Si el nodo de la posición i no tiene hijo derecho, entonces intercambiamos ese nodo con su hijo izquierdo
                swap = data.set(i, data.get(hijoIzq(i)));          //O(1) swap es el valor original en la posición i 
                if (ordenes != null) {                             //O(1) solo se ejecuta durante la inicializacion del heap
                    ordenes[data.get(i).handle()] = i;             //O(1) actualizamos el valor de ordenes en la posición que indica el handle (en este momento indica la posicion de este traslado en traslados)                         
                    ordenes[swap.handle()] = hijoIzq(i);           //O(1) actualizamos el valor de ordenes en la posición que indica el handle (en este momento indica la posicion de este traslado en traslados)  
                }
                i = hijoIzq(i);                                    //O(1)
            }
            else {
                Handler<T> hijoMaximo = max(data.get(hijoIzq(i)), data.get(hijoDer(i)));    //O(1)
                if (ordenes != null)                                                        //O(1)
                    ordenes[hijoMaximo.handle()] = i;                                       //O(1) actualizamos el valor de ordenes en la posición que indica el handle (en este momento indica la posicion de este traslado en traslados)  
                swap = data.set(i, hijoMaximo);                                             //O(1) intercambiamos el nodo de la posición i con el hijo máximo, swap es el valor original en la posición i

                if (prioridad.comparar(data.get(hijoIzq(i)), data.get(hijoDer(i))) > 0) {   //O(1) vemos cual es el hijo de mayor prioridad, y le cambiamos el valor a ordenes en la posición que indica el handle del nuevo hijo izquierdo o derecho por la nueva posición (hijoIzq(i) o hijoDer(i)) de éste.
                    if (ordenes != null)                                                    //O(1)
                        ordenes[swap.handle()] = hijoIzq(i);                                //O(1)
                    i = hijoIzq(i);                                                         //O(1)
                }
                else {                  
                    if (ordenes != null)                                                    //O(1)
                        ordenes[swap.handle()] = hijoDer(i);                                //O(1)
                    i = hijoDer(i);                                                         //O(1)
                }

            }
            data.set(i, swap);                                                              //O(1) en la posición i asignamos el valor swap
        }

        return i;                                                                          //O(1) devuelve la posicion donde quedo el objeto
    }     

    public int tamaño() {    //O(1)
        return tamaño;
    }

    private int hijoIzq(int i) {   //O(1)
        return 2*i + 1;
    }

    private boolean hijosMenores(int i) {     //O(1)
        return (hijoDer(i) >= tamaño || prioridad.comparar(data.get(i), data.get(hijoDer(i))) >= 0) && 
        (hijoIzq(i) >= tamaño || prioridad.comparar(data.get(i), data.get(hijoIzq(i))) >= 0);
    }

    private Handler<T> max(Handler<T> t, Handler<T> s) {   //O(1)
        if (prioridad.comparar(t, s) > 0)
            return t;
        else
            return s;
    }

    private int hijoDer(int i) {   //O(1)
        return 2*i + 2;
    }

    private int padre(int i) {  //O(1)
        return (i-1)/2;
    }
}
