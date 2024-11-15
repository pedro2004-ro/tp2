package aed;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class EstadisticasTests {
    Traslado[] lista;
    Estadisticas estadisticas;
    Ciudad[] ciudades;

    @BeforeEach
    void init() {
        estadisticas = new Estadisticas(7);
        ciudades = new Ciudad[7];                    //O(1)

        for (int i = 0; i < 7; i++) {                //O(C)
            ciudades[i] = new Ciudad();
        }

        lista = new Traslado[] {
            new Traslado(1, 0, 1, 100, 10),
            new Traslado(2, 0, 1, 400, 20),
            new Traslado(3, 3, 4, 500, 50),
            new Traslado(4, 4, 3, 500, 11),
            new Traslado(5, 1, 0, 1000, 40),
            new Traslado(6, 1, 0, 1000, 41),
            new Traslado(7, 6, 3, 2000, 42)
        };
    }

    void assertSetEquals(ArrayList<Integer> s1, ArrayList<Integer> s2) {
        assertEquals(s1.size(), s2.size());
        for (int e1 : s1) {
            boolean encontrado = false;
            for (int e2 : s2) {
                if (e1 == e2) encontrado = true;
            }
            assertTrue(encontrado, "No se encontró el elemento " +  e1 + " en el arreglo " + s2.toString());
        }
    }

    @Test
    void actualizar() {
        for (int i = 0; i < lista.length; i++) {                                //n iteraciones
            Traslado t = lista[i];
            
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

        assertSetEquals(new ArrayList<Integer>(Arrays.asList(1, 6)), estadisticas.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<Integer>(Arrays.asList(3)), estadisticas.ciudadesConMayorPerdida());
        assertEquals(6, estadisticas.ciudadConMayorSuperavit());
    }

}
