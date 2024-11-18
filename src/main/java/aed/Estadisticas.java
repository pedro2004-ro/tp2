package aed;

import java.util.ArrayList;

public class Estadisticas {
    private ArrayList<Integer> ciudadesPorMayorGanancia;     //arrayList con los ids de las ciudades con mayor ganancia 
    private ArrayList<Integer> ciudadesPorMayorPerdida;      //arrayList con los ids de las ciudades con mayor perdida  
    private Heap<Ciudad> ciudadesPorSuperavit;               //heap de los ids de todas las ciudades ordenadas por mayor superavit
    private int[] posicionDeCiudadesPorSuperavit;            //array que indica en que posicion del heap se encuentra cada ciudad 
    private int mayorGanancia;                               
    private int mayorPerdida;
    private int traslados;                                   //cantidad de traslados 
    private int gananciaTotal;

    public Estadisticas(int cantCiudades) {                   // O(C)
        ciudadesPorMayorGanancia = new ArrayList<Integer>();
        for (int i = 0; i < cantCiudades; i++) {             // C iteraciones en todos los for
            ciudadesPorMayorGanancia.add(i);                 // O(1)
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

        ciudadesPorSuperavit = new Heap<Ciudad>(ciudades, new ComparadorPorSuperavit<Handler<Ciudad>>(), ordenes);   // O(C) C=|ciudades| 
        posicionDeCiudadesPorSuperavit = ordenes;     //O(1)

        traslados = 0;             //O(1)
        gananciaTotal = 0;         //O(1)
    }

    public void chequearMaximoGanancia(int indice, int ganancia) {   // O(1)  
        if (ganancia > mayorGanancia) 
            ciudadesPorMayorGanancia.clear();         //si ganancia supera a la mayor ganancia entonces borramos los elementos de ciudadesPorMayorGanancia
        if (ganancia >= mayorGanancia) {
            ciudadesPorMayorGanancia.add(indice);     //agregamos la nueva ciudad a ciudadesPorMayorGanancia      
            mayorGanancia = ganancia;
        }
    }

    public void chequearMaximoPerdida(int indice, int perdida) { //O(1) idem que chequearMaximaGanancia pero con perdida
        if (perdida > mayorPerdida) {
            ciudadesPorMayorPerdida.clear();
        }
        if (perdida >= mayorPerdida) {
            ciudadesPorMayorPerdida.add(indice);
            mayorPerdida = perdida;
        }
    }

    public void chequearMaximoSuperavit(int origen, int destino) {   //O(log(C))
        ciudadesPorSuperavit.revisarPosicion(posicionDeCiudadesPorSuperavit[origen], posicionDeCiudadesPorSuperavit);   //O(log(C))

        ciudadesPorSuperavit.revisarPosicion(posicionDeCiudadesPorSuperavit[destino], posicionDeCiudadesPorSuperavit);  //O(log(C))
    }

    public void aumentarGananciaCiudad(int ciudad, int ganancia) {      //O(1)
        ciudadesPorSuperavit.data().get(posicionDeCiudadesPorSuperavit[ciudad]).dato().aumentarGanancia(ganancia);
    }

    public void aumentarPerdidaCiudad(int ciudad, int perdida) {        //O(1)
        ciudadesPorSuperavit.data().get(posicionDeCiudadesPorSuperavit[ciudad]).dato().aumentarPerdida(perdida);
    }

    public void aumentarGananciaTotal(int g) {                //O(1)
        gananciaTotal = gananciaTotal + g;
    }

    public int ciudadConMayorSuperavit() {                    //O(1)
        return ciudadesPorSuperavit.tope().handle();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia() {    //O(1)
        return ciudadesPorMayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida() {     //O(1)
        return ciudadesPorMayorPerdida;
    }

    public int gananciaPromedioPorTraslado() {                //O(1)
        return gananciaTotal / traslados;
    }

    public void aumentarTraslados() {                         //O(1)
        traslados++;
    }
}
