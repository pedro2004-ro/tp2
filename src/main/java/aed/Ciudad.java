package aed;

public class Ciudad {
    private int ganancia;
    private int perdida;

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

    public int ganancia() {
        return ganancia;
    }

    public int perdida() {
        return perdida;
    }
}
