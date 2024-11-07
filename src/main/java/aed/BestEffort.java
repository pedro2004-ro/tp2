package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<TrasladoHandles> trasladosPorRedito;
    private Heap<TrasladoHandles> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Ciudad[cantCiudades];                    //O(1)

        for (int i = 0; i < cantCiudades; i++) {                //O(C)
            ciudades[i] = new Ciudad();
        }

        estadisticas = new Estadisticas();                      //O(1)

        int[] ordenesPorRedito = new int[traslados.length];     //O(1)
        int[] ordenesPorAntiguedad = new int[traslados.length]; //O(1)

        trasladosPorRedito = new Heap<TrasladoHandles>(traslados, new ComparadorPorRedito<TrasladoHandles>(), ordenesPorRedito);
        trasladosPorAntiguedad = new Heap<TrasladoHandles>(traslados, new ComparadorPorAntiguedad<TrasladoHandles>(), ordenesPorAntiguedad);

        for (int i = 0; i < ordenesPorRedito.length; i++) {                                     //O(T)
            trasladosPorRedito.data.get(ordenesPorRedito[i]).setHandle(ordenesPorAntiguedad[i]);
        }

        for (int i = 0; i < ordenesPorAntiguedad.length; i++) {                                 //O(T)
            trasladosPorAntiguedad.data.get(ordenesPorAntiguedad[i]).setHandle(ordenesPorRedito[i]);
        }
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++) {                                    //O(T)
            TrasladoHandles nuevoRedito = new TrasladoHandles(traslados[i], i);         //O(1)
            int posRedituable = trasladosPorRedito.registrar(nuevoRedito);              //O(log(T))

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, trasladosPorRedito.tamaño() - 1, posRedituable, trasladosPorRedito, trasladosPorAntiguedad);   //O(log(T))

            TrasladoHandles nuevoAntiguedad = new TrasladoHandles(traslados[i], posRedituable);
            int posAntiguedad = trasladosPorAntiguedad.registrar(nuevoAntiguedad);      //O(log(T))

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            trasladosPorRedito.data.get(posRedituable).setHandle(posAntiguedad);

            actualizarHandles(traslados, trasladosPorAntiguedad.tamaño() - 1, posAntiguedad, trasladosPorAntiguedad, trasladosPorRedito);   //O(log(T))

            trasladosPorAntiguedad.data.get(posAntiguedad).setHandle(posRedituable);      //O(1) 
        }
    }

    public int[] despacharMasRedituables(int n){                                            //O(nlog(T) + n) = O(nlog(T))
        if (n > trasladosPorRedito.tamaño()) {                                          
            return despacharMasRedituables(trasladosPorRedito.tamaño());
        }

        Traslado[] traslados = new Traslado[n];                                             //O(1)
        for (int i = 0; i < n; i++) {                                                       //O(n)
            DataDespachado dataDespachadoRedito = trasladosPorRedito.eliminar(0);    //O(log(T))
            traslados[n-i-1] = dataDespachadoRedito.traslado().traslado();                      //O(1)


            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, dataDespachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    //O(log(T))

            if (trasladosPorRedito.tamaño() > 0)
                trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(0).handle()).setHandle(0);
            
            DataDespachado dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(dataDespachadoRedito.traslado().handle()); //O(log(T))

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, dataDespachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);    //O(log(T))

            if (trasladosPorAntiguedad.tamaño() > 0)
                trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(0).handle()).setHandle(0);
        }

        actualizarEstadisticas(traslados);                                                  //O(n)

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {                                                       //O(n)
            res[i] = traslados[i].id;
        }

        return res;
    }

    public int[] despacharMasAntiguos(int n){                                               //O(nlog(T) + n) = O(nlog(T))
        if (n > trasladosPorAntiguedad.tamaño()) {
            return despacharMasRedituables(trasladosPorAntiguedad.tamaño());
        }

        Traslado[] traslados = new Traslado[n];
        for (int i = 0; i < n; i++) {
            DataDespachado dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(0);
            traslados[n-i-1] = dataDespachadoAntiguedad.traslado().traslado();

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, dataDespachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);    //O(log(T))

            if (trasladosPorAntiguedad.tamaño() > 0)
                trasladosPorRedito.data.get(trasladosPorAntiguedad.data.get(0).handle()).setHandle(0);

            DataDespachado dataDespachadoRedito = trasladosPorRedito.eliminar(dataDespachadoAntiguedad.traslado().handle());

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, dataDespachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    //O(log(T))

            if (trasladosPorRedito.tamaño() > 0)
                trasladosPorAntiguedad.data.get(trasladosPorRedito.data.get(0).handle()).setHandle(0);
        }
        actualizarEstadisticas(traslados);

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            res[i] = traslados[i].id;
        }

        return res;
    }

    public int ciudadConMayorSuperavit(){                                                   //O(1)
        return estadisticas.ciudadConMayorSuperavit();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){                                   //O(1)
        return estadisticas.ciudadesConMayorGanancia();
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){                                    //O(1)
        return estadisticas.ciudadesConMayorPerdida();
    }

    public int gananciaPromedioPorTraslado(){                                               //O(1)
        return estadisticas.gananciaPromedioPorTraslado();
    }

    private void actualizarHandles(Traslado[] traslados, int desde, int hasta, Heap<TrasladoHandles> heapEditado, Heap<TrasladoHandles> heapHandles) {  //O(log(T))
        while (desde > hasta) {                                                 //log(T) iteraciones porque recorre la altura
            if (heapEditado.data.get(desde) != null)
                heapHandles.data.get(heapEditado.data.get(desde).handle()).setHandle(desde);
            desde = (desde - 1)/2;
        }
    }
    
    private void actualizarEstadisticas(Traslado[] traslados) {
        for (int i = 0; i < traslados.length; i++) {                                //n iteraciones
            Traslado t = traslados[i];
            
            ciudades[t.origen].aumentarGanancia(t.gananciaNeta);                    //O(1)
            int ganancia = ciudades[t.origen].ganancia();
            int superavitNuevo = ciudades[t.origen].superavit();                    //O(1)
            estadisticas.chequearMaximoGanancia(t.origen, ganancia);                //O(1)
            
            ciudades[t.destino].aumentarPerdida(t.gananciaNeta);                    //O(1)
            int perdida = ciudades[t.destino].perdida();                            //O(1)
            estadisticas.chequearMaximoPerdida(t.destino, perdida);                 //O(1)

            int ciudadConMayorSuperavit = estadisticas.ciudadConMayorSuperavit();   //O(1)
            int superavitMaximo = ciudades[ciudadConMayorSuperavit].superavit();    //O(1)

            estadisticas.chequearMaximoSuperavit(superavitMaximo, superavitNuevo, t.origen);    //O(1)

            estadisticas.aumentarGananciaTotal(t.gananciaNeta);                                 //O(1)
            estadisticas.aumentarTraslados();                                                   //O(1)
        }
    }
}
