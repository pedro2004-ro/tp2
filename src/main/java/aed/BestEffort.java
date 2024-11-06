package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<TrasladoHandles> trasladosPorRedito;
    private Heap<TrasladoHandles> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Ciudad[cantCiudades];

        for (int i = 0; i < cantCiudades; i++) {
            ciudades[i] = new Ciudad();
        }

        estadisticas = new Estadisticas(traslados.length);

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
        Traslado[] traslados = new Traslado[n];
        for (int i = 0; i < n; i++) {
            DataDespachado dataDespachadoRedito = trasladosPorRedito.eliminar(0);
            traslados[n-i-1] = dataDespachadoRedito.traslado.traslado;

            int j = dataDespachadoRedito.posHoja;

            while (j > 0) {
                if (trasladosPorRedito.data.get(j) != null)
                    trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }

            trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
            
            DataDespachado dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(dataDespachadoRedito.traslado.handle);

            j = dataDespachadoAntiguedad.posHoja;

            while (j > 0) {
                if (trasladosPorAntiguedad.data.get(j) != null)
                    trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }

            trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;
        }

        actualizarEstadisticas(traslados);

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            res[i] = traslados[i].id;
        }

        return res;
    }

    public int[] despacharMasAntiguos(int n){
        Traslado[] traslados = new Traslado[n];
        for (int i = 0; i < n; i++) {
            DataDespachado dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(0);
            traslados[n-i-1] = dataDespachadoAntiguedad.traslado.traslado;

            int j = dataDespachadoAntiguedad.posHoja;

            while (j > 0) {
                if (trasladosPorAntiguedad.data.get(j) != null)
                    trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;
                j = (j - 1)/2;
            }

            trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(j).handle).handle = j;

            DataDespachado dataDespachadoRedito = trasladosPorRedito.eliminar(dataDespachadoAntiguedad.traslado.handle);

            j = dataDespachadoRedito.posHoja;

            while (j > 0) {
                if (trasladosPorRedito.data.get(j) != null) {
                    trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
                }
                j = (j - 1)/2;
            }

            trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(j).handle).handle = j;
        }
        actualizarEstadisticas(traslados);

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            res[i] = traslados[i].id;
        }

        return res;
    }

    public int ciudadConMayorSuperavit(){
        return estadisticas.ciudadConMayorSuperavit();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return estadisticas.ciudadesConMayorGanancia();
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return estadisticas.ciudadesConMayorPerdida();
    }

    public int gananciaPromedioPorTraslado(){
        return estadisticas.gananciaPromedioPorTraslado();
    }
    
    private void actualizarEstadisticas(Traslado[] traslados) {
        for (int i = 0; i < traslados.length; i++) {
            Traslado t = traslados[i];
            
            ciudades[t.origen].aumentarGanancia(t.gananciaNeta);
            int ganancia = ciudades[t.origen].ganancia;
            int superavitNuevo = ciudades[t.origen].superavit();
            estadisticas.chequearMaximoGanancia(t.origen, ganancia);
            
            ciudades[t.destino].aumentarPerdida(t.gananciaNeta);
            int perdida = ciudades[t.destino].perdida;
            estadisticas.chequearMaximoPerdida(t.destino, perdida);

            int ciudadConMayorSuperavit = estadisticas.ciudadConMayorSuperavit();
            int superavitMaximo = ciudades[ciudadConMayorSuperavit].superavit();

            estadisticas.chequearMaximoSuperavit(superavitMaximo, superavitNuevo, t.origen);

            estadisticas.aumentarGananciaTotal(t.gananciaNeta);
        }
    }
}
