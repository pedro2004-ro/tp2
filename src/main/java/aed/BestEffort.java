package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<TrasladoHandles> trasladosPorRedito;
    private Heap<TrasladoHandles> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        System.out.println("hola");
        ciudades = new Ciudad[cantCiudades];

        int[] ordenesPorRedito = new int[traslados.length];
        int[] ordenesPorAntiguedad = new int[traslados.length];

        trasladosPorRedito = new Heap<TrasladoHandles>(traslados, new ComparadorPorRedito<TrasladoHandles>(), ordenesPorRedito);
        trasladosPorAntiguedad = new Heap<TrasladoHandles>(traslados, new ComparadorPorAntiguedad<TrasladoHandles>(), ordenesPorAntiguedad);

        for (int i = 0; i < ordenesPorRedito.length; i++) {
            trasladosPorRedito.data.get(ordenesPorRedito[i]).handle = ordenesPorAntiguedad[i];
        }

        for (int i = 0; i < ordenesPorAntiguedad.length; i++) {
            trasladosPorAntiguedad.data.get(ordenesPorAntiguedad[i]).handle = ordenesPorRedito[i];
        }
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++) {
            TrasladoHandles nuevoRedito = new TrasladoHandles(traslados[i], i);
            int posRedituable = trasladosPorRedito.registrar(nuevoRedito);
            
            System.out.println("registrando traslados");

            int j = trasladosPorRedito.tamaño - 1;

            while (j > posRedituable) {
                trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }

            TrasladoHandles nuevoAntiguedad = new TrasladoHandles(traslados[i], posRedituable);
            int posAntiguedad = trasladosPorAntiguedad.registrar(nuevoAntiguedad);

            j = trasladosPorAntiguedad.tamaño - 1;

            while (j > posAntiguedad) {
                trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }

            trasladosPorRedito.data.get(posRedituable).handle = posAntiguedad;            
        }
    }

    public int[] despacharMasRedituables(int n){
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            DataDespachado dataDespachadoRedito = trasladosPorRedito.eliminar(0);
            res[n-i-1] = dataDespachadoRedito.idDespachado;

            int j = dataDespachadoRedito.posHoja;

            while (j > 0) {
                trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }
            
            DataDespachado dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(dataDespachadoRedito.handleDespachado);

            j = dataDespachadoAntiguedad.posHoja;

            while (j > 0) {
                trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }
        }
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
