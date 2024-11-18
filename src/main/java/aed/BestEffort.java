package aed;

import java.util.ArrayList;

public class BestEffort {
    private Heap<Traslado> trasladosPorRedito;           //los traslados en una cola de prioridad ordenado por redito con un handle que indica su posicion en antiguedad
    private Heap<Traslado> trasladosPorAntiguedad;       //los traslados en una cola de prioridad ordenado por antiguedad con un handle que indica su posicion en redito
    private Ciudad[] ciudades;                           //lista de ciudades ordenadas por id 
    private Estadisticas estadisticas;                   //modulo que nos permite llevar las estadisticas 

    // C es igual a la longitud de la cantidad de ciudades, T la cantidad de traslados
    public BestEffort(int cantCiudades, Traslado[] traslados){ // O(T + C)
        ciudades = new Ciudad[cantCiudades];                    //O(1)

        for (int i = 0; i < cantCiudades; i++) {                // C iteraciones, y como lo de adentro del for es O(1), el costo es O(C)
            ciudades[i] = new Ciudad();                         // O(1)
        }

        estadisticas = new Estadisticas(cantCiudades);          //O(1)

        // creamos un array que representa la posicion de los traslados en el heap cuando estan ordenados por redito y por antiguedad
        int[] ordenesPorRedito = new int[traslados.length];     //O(1) 
        int[] ordenesPorAntiguedad = new int[traslados.length]; //O(1)

        trasladosPorRedito = new Heap<Traslado>(traslados, new ComparadorPorRedito<Handler<Traslado>>(), ordenesPorRedito); //O(|traslados|)
        trasladosPorAntiguedad = new Heap<Traslado>(traslados, new ComparadorPorAntiguedad<Handler<Traslado>>(), ordenesPorAntiguedad); //O(|traslados|)

        //ponemos en los handles de cada traslado su posicion el el otro heap
        for (int i = 0; i < ordenesPorRedito.length; i++) {                                     //O(T)
            trasladosPorRedito.data().get(ordenesPorRedito[i]).setHandle(ordenesPorAntiguedad[i]);
        }

        for (int i = 0; i < ordenesPorAntiguedad.length; i++) {                                 //O(T)
            trasladosPorAntiguedad.data().get(ordenesPorAntiguedad[i]).setHandle(ordenesPorRedito[i]);
        }
    }

    public void registrarTraslados(Traslado[] traslados){                               // O(|traslados|log(T))
        for (int i = 0; i < traslados.length; i++) {                                    // |traslados| iteraciones
            Handler<Traslado> nuevoRedito = new Handler<Traslado>(traslados[i], i);         //O(1)
            int posRedituable = trasladosPorRedito.registrar(nuevoRedito);              //O(log(T)) posRedituable es la posicion en la que quedo nuevoRedito en el heap de redito

            /*hay un detalle: como insertamos al elemento en la ultima posicion y lo vamos cambiando por el padre,
             * sabemos que en el peor caso alteramos el orden de una sola rama del heap. Por lo tanto podemos actualizar los handles 
             * iterando sobre la altura del heap 
             */
            actualizarHandles(traslados, trasladosPorRedito.tamaño() - 1, posRedituable, trasladosPorRedito, trasladosPorAntiguedad);   //O(log(T))

            Handler<Traslado> nuevoAntiguedad = new Handler<Traslado>(traslados[i], posRedituable); 
            int posAntiguedad = trasladosPorAntiguedad.registrar(nuevoAntiguedad);      //O(log(T)) idem de arriba con antiguedad 

            trasladosPorRedito.data().get(posRedituable).setHandle(posAntiguedad); // O(1) le cambiamos el handle a nuevo redito y le ponemos su posicion en el heap de antiguedad

            actualizarHandles(traslados, trasladosPorAntiguedad.tamaño() - 1, posAntiguedad, trasladosPorAntiguedad, trasladosPorRedito);   //O(log(T))

            trasladosPorAntiguedad.data().get(posAntiguedad).setHandle(posRedituable);      //O(1) le cambiamos el handle al nuevo traslado en antiguedad y le ponemos su posicion en el heap de redito
        }
    }

    public int[] despacharMasRedituables(int n){                                            //O(n(log(T) + log(C)))
        if (n == 0) {                                                                       // O(1)
            return new int[0];
        }

        if (n > trasladosPorRedito.tamaño()) {                                              //si nos piden despachar mas que la cantidad de traslados que hay en el heap, despachamos todos los traslados 
            return despacharMasRedituables(trasladosPorRedito.tamaño());
        }

        Traslado[] traslados = new Traslado[n];                                             //O(1)
        for (int i = 0; i < n; i++) {                                                       // n iteraciones
            Despachado<Traslado> despachadoRedito = trasladosPorRedito.eliminar(0);    //O(log(T)) nos devuelve el handler del traslado y la posicion donde lo dejo en el heap
            traslados[i] = despachadoRedito.dato().dato();                                   //O(1)

            /*hay un detalle: como despues de desencolar movemos el ultimo elemento a la posicion 0 y lo vamos cambiando por el hijo con mas prioridad,
             * sabemos que en el peor caso alteramos el orden de una sola rama del heap. Por lo tanto podemos actualizar los handles 
             * iterando sobre la altura del heap 
             */
            actualizarHandles(traslados, despachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    //O(log(T))

            if (trasladosPorRedito.tamaño() > 0)                       // cambiamos el handle del primer elemento solamente cuando el tamaño es mayor a 0 
                trasladosPorAntiguedad.data().get(trasladosPorRedito.data().get(0).handle()).setHandle(0);     //O(1)
            
            Despachado<Traslado> despachadoAntiguedad = trasladosPorAntiguedad.eliminar(despachadoRedito.dato().handle()); //O(log(T)) nos devuelve el handler del traslado y la posicion donde lo dejo en el heap

            actualizarHandles(traslados, despachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);    //O(log(T))

            if (trasladosPorAntiguedad.tamaño() > 0)                   // cambiamos el handle del primer elemento solamente cuando el tamaño es mayor a 0 
                trasladosPorRedito.data().get(trasladosPorAntiguedad.data().get(0).handle()).setHandle(0);      //O(1)
        }

        actualizarEstadisticas(traslados);                                                  //O(nlog(C))

        int[] res = new int[n];

        for (int i = 0; i < n; i++) {                                                       //O(n)
            res[i] = traslados[i].id;
        }

        return res;                                                                         //O(1)
    }

    public int[] despacharMasAntiguos(int n){                                               //O(n(log(T) + log(C))) mismo funcionamiento que despachar mas redituables
        if (n == 0) {
            return new int[0];                                                             
        }

        if (n > trasladosPorAntiguedad.tamaño()) {
            return despacharMasAntiguos(trasladosPorAntiguedad.tamaño());               
        }

        Traslado[] traslados = new Traslado[n];
        for (int i = 0; i < n; i++) {
            Despachado<Traslado> despachadoAntiguedad = trasladosPorAntiguedad.eliminar(0);
            traslados[i] = despachadoAntiguedad.dato().dato();

            actualizarHandles(traslados, despachadoAntiguedad.posHoja(), 0, trasladosPorAntiguedad, trasladosPorRedito);  

            if (trasladosPorAntiguedad.tamaño() > 0)
                trasladosPorRedito.data().get(trasladosPorAntiguedad.data().get(0).handle()).setHandle(0);

            Despachado<Traslado> despachadoRedito = trasladosPorRedito.eliminar(despachadoAntiguedad.dato().handle());

            actualizarHandles(traslados, despachadoRedito.posHoja(), 0, trasladosPorRedito, trasladosPorAntiguedad);    

            if (trasladosPorRedito.tamaño() > 0)
                trasladosPorAntiguedad.data().get(trasladosPorRedito.data().get(0).handle()).setHandle(0);
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
    
    // recibe una lista de traslados, las posiciones que hay que corregir, el heap donde se inserto el elemento y el heap al que le vamos a cambiar los handles.
    private void actualizarHandles(Traslado[] traslados, int desde, int hasta, Heap<Traslado> heapEditado, Heap<Traslado> heapHandles) {  //O(log(T))
        while (desde > hasta) {                                                 //log(T) iteraciones porque recorre la altura
            if (heapEditado.data().get(desde) != null){                        //si la posicion del heap esta ocupada por un traslado 
            /*mira heap editado en la posicion desde. Obtiene su handle que indica la posicion de este objeto en heapHandles
             * indexa heapHandles en esa posicion y cambia este handle a desde 
             */
                heapHandles.data().get(heapEditado.data().get(desde).handle()).setHandle(desde);   //O(1)    
            }
            desde = (desde - 1)/2;                       //O(1) lo cambia al padre
        }
    }
    
    private void actualizarEstadisticas(Traslado[] traslados) {             //O(nlog(C))
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
