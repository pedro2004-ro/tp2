package aed;

public class Ciudad {
    private int ganancia;
    private int perdida;

    public Ciudad() {                          //O(1)
        ganancia = 0;
        perdida = 0;
    }

    public int superavit() {                  //O(1)
        return ganancia - perdida;
    }

    public void aumentarGanancia(int g) {     //O(1)
        ganancia = ganancia + g;
    }

    public void aumentarPerdida(int p) {      //O(1)
        perdida = perdida + p;
    }

    public int ganancia() {                   //O(1)
        return ganancia;
    }

    public int perdida() {                   //O(1)
        return perdida;
    }
}
