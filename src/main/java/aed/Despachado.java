package aed;

//es una clase especial que se utiliza cuando despachamos un objeto que nos devuelve este mismo y la posicion donde quedo el ultimo elemento luego de hacerle siftdown
public class Despachado<T> {
    private Handler<T> dato;     
    private int posHoja;        

    public Despachado(Handler<T> t, int pos) { //O(1)
        dato = t;
        posHoja = pos;
    }

    public Handler<T> dato() {     //O(1)
        return dato;
    }

    public int posHoja() {         //O(1)
        return posHoja;
    }
}
