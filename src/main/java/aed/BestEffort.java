package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<Traslado> trasladosPorRedito;
    private Heap<Traslado> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Ciudad[cantCiudades];
        
        class ComparadorPorRedito<T> implements Comparador<Traslado> {
            @Override
            public int comparar(Traslado a, Traslado b) {
                if (a.gananciaNeta > b.gananciaNeta)
                    return 1;
                else if (a.gananciaNeta == b.gananciaNeta) {
                    if (a.id < b.id) 
                        return 1;
                    else 
                        return -1;
                }
                else 
                    return -1;
            }
        }

        trasladosPorRedito = new Heap<Traslado>(traslados, new ComparadorPorRedito<Traslado>());

        class ComparadorPorAntiguedad<T> implements Comparador<Traslado> {
            @Override
            public int comparar(Traslado a, Traslado b) {
                if (a.timestamp < b.timestamp) {
                    return 1;
                }
                else
                    return -1;
            }
        }

        trasladosPorAntiguedad = new Heap<Traslado>(traslados, new ComparadorPorAntiguedad<Traslado>());
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
