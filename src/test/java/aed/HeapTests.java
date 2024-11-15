package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestTag;
import org.junit.jupiter.api.BeforeEach;

public class HeapTests {
    int cantCiudades;
    Traslado[] listaTraslados;
    ArrayList<Integer> actual;

    @BeforeEach
    void init() {
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

    @Test
    void heapificar() {
        int[] ordenesPorAntiguedad = new int[listaTraslados.length];
        Heap<Traslado> HeapTest = new Heap<Traslado>(listaTraslados, new ComparadorPorAntiguedad<Traslado>(), ordenesPorAntiguedad);

        ArrayList<Handler<Traslado>> arrayTraslados;
        arrayTraslados = HeapTest.data();

        int[] arrayTrasladosTimestamp = new int[listaTraslados.length];

        for (int i = 0; i < listaTraslados.length; i++) {
            arrayTrasladosTimestamp[i] = arrayTraslados.get(i).dato().timestamp;
        }

        int[] expected = new int[] {10,11,41,20,40,50,42};

        assertArrayEquals(expected, arrayTrasladosTimestamp);

    }

    @Test
    void encolarHeapVacio() {
        Traslado[] t = new Traslado[] {};
        int[] o = new int[] {};
        Heap<Traslado> HeapTest = new Heap<Traslado>(t, new ComparadorPorRedito<Traslado>(), o);

        Traslado nuevo = new Traslado(2, 1, 2, 10000, 2);

        Handler<Traslado> nuevoHandler = new Handler<>(nuevo, 0);

        ArrayList<Handler<Traslado>> arrayTraslados;
        arrayTraslados = HeapTest.data();
        
        HeapTest.registrar(nuevoHandler);

        int[] arrayTrasladosGanancia = new int[1];

        arrayTrasladosGanancia[0] = arrayTraslados.get(0).dato().gananciaNeta;

        int[] expected = new int[] {10000};

        assertArrayEquals(expected, arrayTrasladosGanancia);
    }

    @Test
    void encolarMaximo() {
        int[] ordenesPorAntiguedad = new int[listaTraslados.length];
        Heap<Traslado> HeapTest = new Heap<Traslado>(listaTraslados, new ComparadorPorAntiguedad<Traslado>(), ordenesPorAntiguedad);

        Traslado nuevo = new Traslado(2, 1, 2, 10000, 2);

        Handler<Traslado> nuevoHandler = new Handler<>(nuevo, 0);

        HeapTest.registrar(nuevoHandler);

        ArrayList<Handler<Traslado>> arrayTraslados;
        arrayTraslados = HeapTest.data();
        
        int[] arrayTrasladosTimestamp = new int[8];

        for (int i = 0; i < 8; i++) {
            arrayTrasladosTimestamp[i] = arrayTraslados.get(i).dato().timestamp;
        }

        int[] expected = new int[] {2,10,41,11,40,50,42,20};

        assertArrayEquals(expected, arrayTrasladosTimestamp);
    }

    @Test 
    void desencolarTodos() {
        Traslado[] traslados = new Traslado[] {
            new Traslado(1, 0, 1, 100, 10),
            new Traslado(2, 0, 1, 400, 20),
            new Traslado(3, 3, 4, 500, 50),
        };

        Heap<Traslado> h = new Heap<>(traslados, new ComparadorPorRedito<Traslado>(), new int[3]);

        assertEquals(3, h.eliminar(0).dato().dato().id);
        assertEquals(2, h.eliminar(0).dato().dato().id);
        assertEquals(1, h.eliminar(0).dato().dato().id);

        ArrayList<Handler<Traslado>> t = h.data();

        for (int i = 0; i < 3; i++) {
            assertEquals(null, t.get(i));
        }
    }
}
