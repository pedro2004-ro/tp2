package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class BETestsPropios {
    int cantCiudades;
    Traslado[] listaTraslados;
    ArrayList<Integer> actual;


    @BeforeEach
    void init(){
        //Reiniciamos los valores de las ciudades y traslados antes de cada test
        cantCiudades = 7;
        listaTraslados = new Traslado[] {
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
    void sin_traslados() {
        Traslado[] nuevos = new Traslado[] {};

        BestEffort sis = new BestEffort(cantCiudades, nuevos);

        assertEquals(null, sis.despacharMasAntiguos(5));

        assertTrue(0 <= sis.ciudadConMayorSuperavit() && sis.ciudadConMayorSuperavit() < cantCiudades);
    }

    @Test
    void registrar_nada() {
        Traslado[] nuevos = new Traslado[] {};

        BestEffort sis = new BestEffort(cantCiudades, listaTraslados);

        sis.registrarTraslados(nuevos);

        int[] expected = new int[] {1,4};

        assertArrayEquals(expected, sis.despacharMasAntiguos(2));

        expected = new int[] {2,5,6,7,3};

        assertArrayEquals(expected, sis.despacharMasAntiguos(8));
    }

    @Test
    void romper_superavit_propio() {
        Traslado[] nuevos = new Traslado[] {
            new Traslado(8, 5, 2, 1452, 5),
            new Traslado(9, 1, 6, 1200, 2),
            new Traslado(10, 0, 3, 500, 3),
            new Traslado(11, 5, 4, 333, 4),
        };

        BestEffort sis = new BestEffort(this.cantCiudades, nuevos);

        int[] expected = new int[] {9, 10};

        assertArrayEquals(sis.despacharMasAntiguos(2), expected);                   //este test nos ayudó a descubrir un error con el array2heap.

        expected = new int[] {8, 11};

        assertArrayEquals(sis.despacharMasRedituables(2), expected);

        assertEquals(5, sis.ciudadConMayorSuperavit());

        nuevos = new Traslado[] {
            new Traslado(12, 2, 5, 2000, 9)
        };

        sis.registrarTraslados(nuevos);

        expected = new int[] {12};

        assertArrayEquals(sis.despacharMasRedituables(2), expected);

        assertEquals(1, sis.ciudadConMayorSuperavit());
    }

}
