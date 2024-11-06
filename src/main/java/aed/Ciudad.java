package aed;

public class Ciudad {
    public int ganancia;
    public int perdida;

    public Ciudad() {
        ganancia = 0;
        perdida = 0;
    }

    public int superavit() {
        return ganancia - perdida;
    }

    public void aumentarGanancia(int g) {
        ganancia = ganancia + g;
    }

    public void aumentarPerdida(int p) {
        perdida = perdida + p;
    }
}
