package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<TrasladoHandles> trasladosPorRedito;
    private Heap<TrasladoHandles> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Ciudad[cantCiudades];

        trasladosPorRedito = new Heap<TrasladoHandles>(traslados, new ComparadorPorRedito<TrasladoHandles>());
        trasladosPorAntiguedad = new Heap<TrasladoHandles>(traslados, new ComparadorPorAntiguedad<TrasladoHandles>());
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++) {
            int posRedituable = trasladosPorRedito.registrar(traslados[i]);
        }
    }

    public int[] despacharMasRedituables(int n){
        // Implementar
        return null;
    }

    public int[] despacharMasAntiguos(int n){
        // Implementar
        return null;
    }

    public int ciudadConMayorSuperavit(){
        // Implementar
        return 0;
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        // Implementar
        return null;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        // Implementar
        return null;
    }

    public int gananciaPromedioPorTraslado(){
        // Implementar
        return 0;
    }
    
}
