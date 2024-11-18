package aed;
 
//creamos esta clase para indicar en el caso de los traslados su posicion en el otro heap
//en cuanto a ciudades el handle indica el id de la ciudad
public class Handler<T> {
    private T dato;             
    private int handle;

    public Handler(T t, int h) {      //O(1)
        dato = t;
        handle = h;
    }

    public T dato() {                 //O(1)
        return dato;
    }

    public int handle() {             //O(1)
        return handle;
    }

    public void setHandle(int h) {    //O(1)
        handle = h;
    }
}