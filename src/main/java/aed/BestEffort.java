package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<Traslado> trasladosPorRedito;
    private Heap<Traslado> trasladosPorAntiguedad;
    private Ciudad[] ciudades;
    private Estadisticas estadisticas;

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Ciudad[cantCiudades];                    //O(1)

        for (int i = 0; i < cantCiudades; i++) {                // |C| iteraciones, y como lo de adentro del for es O(1), el costo es O(|C|)
            ciudades[i] = new Ciudad();                         // O(1)
        }

        estadisticas = new Estadisticas(cantCiudades);          //O(1)

        int[] ordenesPorRedito = new int[traslados.length];     //O(1)
        int[] ordenesPorAntiguedad = new int[traslados.length]; //O(1)

        trasladosPorRedito = new Heap<Traslado>(traslados, new ComparadorPorRedito<Handler<Traslado>>(), ordenesPorRedito);  
        trasladosPorAntiguedad = new Heap<Traslado>(traslados, new ComparadorPorAntiguedad<Handler<Traslado>>(), ordenesPorAntiguedad);

        for (int i = 0; i < ordenesPorRedito.length; i++) {                                     //O(T)
            trasladosPorRedito.data().get(ordenesPorRedito[i]).setHandle(ordenesPorAntiguedad[i]);
        }

        for (int i = 0; i < ordenesPorAntiguedad.length; i++) {                                 //O(T)
            trasladosPorAntiguedad.data().get(ordenesPorAntiguedad[i]).setHandle(ordenesPorRedito[i]);
        }
    }

    public void registrarTraslados(Traslado[] traslados){
        for (int i = 0; i < traslados.length; i++) {                                    //O(T)
            Handler<Traslado> nuevoRedito = new Handler<Traslado>(traslados[i], i);         //O(1)
            int posRedituable = trasladosPorRedito.registrar(nuevoRedito);              //O(log(T))

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            actualizarHandles(traslados, trasladosPorRedito.tamaño() - 1, posRedituable, trasladosPorRedito, trasladosPorAntiguedad);   //O(log(T))

            Handler<Traslado> nuevoAntiguedad = new Handler<Traslado>(traslados[i], posRedituable);
            int posAntiguedad = trasladosPorAntiguedad.registrar(nuevoAntiguedad);      //O(log(T))

            //HEAP IMPLEMENTA ESTE PROCEDIMIENTO!!!!

            trasladosPorRedito.data().get(posRedituable).setHandle(posAntiguedad);

            actualizarHandles(traslados, trasladosPorAntiguedad.tamaño() - 1, posAntiguedad, trasladosPorAntiguedad, trasladosPorRedito);   //O(log(T))

            trasladosPorAntiguedad.data().get(posAntiguedad).setHandle(posRedituable);      //O(1) 
        }
    }

    public int[] despacharMasRedituables(int n){                                            //O(n(log(T) + log(C)))
        if (n == 0) {
            return null;
        }

        if (n > trasladosPorRedito.tamaño()) {                                          
            return despacharMasRedituables(trasladosPorRedito.tamaño());
        }

        Traslado[] traslados = new Traslado[n];                                             //O(1)
        for (int i = 0; i < n; i++) {                                                       //O(n)
            Despachado<Traslado> dataDespachadoRedito = trasladosPorRedito.eliminar(0);    //O(log(T))
            traslados[i] = dataDespachadoRedito.dato().dato();                      //O(1)

            actualizarHandles(traslados, dataDespachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    //O(log(T))

            if (trasladosPorRedito.tamaño() > 0)
                trasladosPorAntiguedad.data().get(trasladosPorRedito.data().get(0).handle()).setHandle(0);
            
            Despachado<Traslado> dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(dataDespachadoRedito.dato().handle()); //O(log(T))

            actualizarHandles(traslados, dataDespachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);    //O(log(T))

            if (trasladosPorAntiguedad.tamaño() > 0)
                trasladosPorRedito.data().get(trasladosPorAntiguedad.data().get(0).handle()).setHandle(0);
        }

        actualizarEstadisticas(traslados);                                                  //O(nlog(C))

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {                                                       //O(n)
            res[i] = traslados[i].id;
        }

        return res;
    }

    public int[] despacharMasAntiguos(int n){                                               //O(n(log(T) + log(C)))
        if (n == 0) {
            return null;                                                                    //Salida nula si el input es 0
        }

        if (n > trasladosPorAntiguedad.tamaño()) {
            return despacharMasAntiguos(trasladosPorAntiguedad.tamaño());                   //Para evitar traslados nulos
        }

        Traslado[] traslados = new Traslado[n];
        for (int i = 0; i < n; i++) {
            Despachado<Traslado> dataDespachadoAntiguedad = trasladosPorAntiguedad.eliminar(0);
            traslados[i] = dataDespachadoAntiguedad.dato().dato();

            actualizarHandles(traslados, dataDespachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);    //O(log(T))

            if (trasladosPorAntiguedad.tamaño() > 0)
                trasladosPorRedito.data().get(trasladosPorAntiguedad.data().get(0).handle()).setHandle(0);

            Despachado<Traslado> dataDespachadoRedito = trasladosPorRedito.eliminar(dataDespachadoAntiguedad.dato().handle());

            actualizarHandles(traslados, dataDespachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    //O(log(T))

            if (trasladosPorRedito.tamaño() > 0)
                trasladosPorAntiguedad.data().get(trasladosPorRedito.data().get(0).handle()).setHandle(0);
        }
        actualizarEstadisticas(traslados);                                                                                        //O(nlog(C))

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

    @Override
    public String toString() {
        String res = "";
        res += trasladosPorAntiguedad;
        return res;
    }

    private void actualizarHandles(Traslado[] traslados, int desde, int hasta, Heap<Traslado> heapEditado, Heap<Traslado> heapHandles) {  //O(log(T))
        while (desde > hasta) {                                                 //log(T) iteraciones porque recorre la altura
            if (heapEditado.data().get(desde) != null)
                heapHandles.data().get(heapEditado.data().get(desde).handle()).setHandle(desde);
            desde = (desde - 1)/2;
        }
    }
    
    private void actualizarEstadisticas(Traslado[] traslados) {
        for (int i = 0; i < traslados.length; i++) {                                //n iteraciones
            Traslado t = traslados[i];
            
            if (t.origen != t.destino) {
                ciudades[t.origen].aumentarGanancia(t.gananciaNeta);                    //O(1)
                estadisticas.aumentarGananciaCiudad(t.origen, t.gananciaNeta);
                
                int ganancia = ciudades[t.origen].ganancia();
                estadisticas.chequearMaximoGanancia(t.origen, ganancia);                //O(1)
                
                ciudades[t.destino].aumentarPerdida(t.gananciaNeta);                    //O(1)
                estadisticas.aumentarPerdidaCiudad(t.destino, t.gananciaNeta);

                int perdida = ciudades[t.destino].perdida();                            //O(1)
                estadisticas.chequearMaximoPerdida(t.destino, perdida);                 //O(1)


                estadisticas.chequearMaximoSuperavit(t.origen, t.destino);              //O(log(C))
            }
            
            estadisticas.aumentarGananciaTotal(t.gananciaNeta);                                 //O(1)
            estadisticas.aumentarTraslados();                                                   //O(1)
        }
    }
}
