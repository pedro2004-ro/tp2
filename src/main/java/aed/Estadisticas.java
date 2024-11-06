package aed;

import java.util.ArrayList;

public class Estadisticas {
    private ArrayList<Integer> ciudadesPorMayorGanancia;
    private int mayorGanancia;
    private ArrayList<Integer> ciudadesPorMayorPerdida;
    private int mayorPerdida;
    private int ciudadMayorSuperavit;
    private int mayorSuperavit;
    private int traslados;
    private int gananciaTotal;

    public Estadisticas(int t) {
        ciudadesPorMayorGanancia = new ArrayList<Integer>();
        ciudadesPorMayorGanancia.add(0);
        mayorGanancia = 0;

        ciudadesPorMayorPerdida = new ArrayList<Integer>();
        ciudadesPorMayorPerdida.add(0);
        mayorPerdida = 0;

        ciudadMayorSuperavit = 0;
        mayorSuperavit = 0;

        traslados = t;
        gananciaTotal = 0;
    }

    public void chequearMaximoGanancia(int indice, int ganancia) {
        if (ganancia > mayorGanancia) 
            ciudadesPorMayorGanancia.clear();
        if (ganancia >= mayorGanancia)
            ciudadesPorMayorGanancia.add(indice);
            mayorGanancia = ganancia;
    }

    public void chequearMaximoPerdida(int indice, int perdida) {
        if (perdida > mayorPerdida) {
            ciudadesPorMayorPerdida.clear();
        }
        if (perdida >= mayorPerdida) {
            ciudadesPorMayorPerdida.add(indice);
            mayorPerdida = perdida;
        }
    }

    public void chequearMaximoSuperavit(int superavitMaximo, int superavitNuevo, int indice) {
        if (superavitNuevo > superavitMaximo) {
            mayorSuperavit = superavitNuevo;
            ciudadMayorSuperavit = indice;
        }

        if (superavitNuevo == superavitMaximo) {
            if (indice < ciudadMayorSuperavit) {
                mayorSuperavit = superavitNuevo;
                ciudadMayorSuperavit = indice;
            }
            else {
                mayorSuperavit = superavitMaximo;
            }
        }

        if (superavitMaximo > superavitNuevo) {
            mayorSuperavit = superavitMaximo;
        }
    }

    public void aumentarGananciaTotal(int g) {
        gananciaTotal = gananciaTotal + g;
    }

    public int ciudadConMayorSuperavit() {
        return ciudadMayorSuperavit;
    }

    public ArrayList<Integer> ciudadesConMayorGanancia() {
        return ciudadesPorMayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida() {
        return ciudadesPorMayorPerdida;
    }

    public int gananciaPromedioPorTraslado() {
        return gananciaTotal / traslados;
    }
}
