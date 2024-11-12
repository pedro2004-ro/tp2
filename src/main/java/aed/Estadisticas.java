package aed;

import java.util.ArrayList;

public class Estadisticas {
    private ArrayList<Integer> ciudadesPorMayorGanancia;
    private ArrayList<Integer> ciudadesPorMayorPerdida;
    private Heap<Ciudad> ciudadesPorSuperavit;
    private int[] posicionDeCiudadesPorSuperavit;
    private int mayorGanancia;
    private int mayorPerdida;
    private int traslados;
    private int gananciaTotal;

    public Estadisticas(int cantCiudades) {
        ciudadesPorMayorGanancia = new ArrayList<Integer>();
        for (int i = 0; i < cantCiudades; i++) {
            ciudadesPorMayorGanancia.add(i);
        }
        mayorGanancia = 0;

        ciudadesPorMayorPerdida = new ArrayList<Integer>();
        for (int i = 0; i < cantCiudades; i++) {
            ciudadesPorMayorPerdida.add(i);
        }
        mayorPerdida = 0;

        Ciudad[] ciudades = new Ciudad[cantCiudades];
        int[] ordenes = new int[cantCiudades];

        for (int i = 0; i < cantCiudades; i++) {
            ciudades[i] = new Ciudad();
            ordenes[i] = i;
        }

        ciudadesPorSuperavit = new Heap<Ciudad>(ciudades, new ComparadorPorSuperavit<Handler<Ciudad>>(), ordenes);
        posicionDeCiudadesPorSuperavit = ordenes;

        traslados = 0;
        gananciaTotal = 0;
    }

    public void chequearMaximoGanancia(int indice, int ganancia) {
        if (ganancia > mayorGanancia) 
            ciudadesPorMayorGanancia.clear();
        if (ganancia >= mayorGanancia) {
            ciudadesPorMayorGanancia.add(indice);
            mayorGanancia = ganancia;
        }
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

    public void chequearMaximoSuperavit(int origen, int destino) {
        ciudadesPorSuperavit.revisarPosicion(posicionDeCiudadesPorSuperavit[origen], posicionDeCiudadesPorSuperavit);   //O(log(C))

        ciudadesPorSuperavit.revisarPosicion(posicionDeCiudadesPorSuperavit[destino], posicionDeCiudadesPorSuperavit);  //O(log(C))
    }

    public void aumentarGananciaCiudad(int ciudad, int ganancia) {
        ciudadesPorSuperavit.data().get(posicionDeCiudadesPorSuperavit[ciudad]).dato().aumentarGanancia(ganancia);
    }

    public void aumentarPerdidaCiudad(int ciudad, int perdida) {
        ciudadesPorSuperavit.data().get(posicionDeCiudadesPorSuperavit[ciudad]).dato().aumentarPerdida(perdida);
    }

    public void aumentarGananciaTotal(int g) {
        gananciaTotal = gananciaTotal + g;
    }

    public int ciudadConMayorSuperavit() {
        return ciudadesPorSuperavit.tope().handle();
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

    public void aumentarTraslados() {
        traslados++;
    }
}
