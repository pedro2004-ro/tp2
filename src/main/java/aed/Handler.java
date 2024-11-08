package aed;

public class Handler<T> {
    private T dato;
    private int handle;

    public Handler(T t, int h) {
        dato = t;
        handle = h;
    }

    public T dato() {
        return dato;
    }

    public int handle() {
        return handle;
    }

    public void setHandle(int h) {
        handle = h;
    }
}