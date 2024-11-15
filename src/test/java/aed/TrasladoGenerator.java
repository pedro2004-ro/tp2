package aed;

public class TrasladoGenerator {
    static int a = 32;
    static int c = 7;
    static int m = 155871;

    private static Traslado generarTraslado(int seed, int cantCiudades) {   
        int id = (a*seed + c) % m;
        int gananciaNeta = (a*id + c) % m;
        int timestamp = (a*gananciaNeta + c) % m;

        int origen = (a*timestamp + c) % cantCiudades;

        int destino = (a*origen + c) % cantCiudades;

        if (destino == origen)
            destino++; 
        
        return new Traslado(id, origen, destino, gananciaNeta, timestamp);
    }

    public static Traslado[] generarNTraslados(int seed, int cantCiudades, int cantTraslados) {
        Traslado[] res = new Traslado[cantTraslados];
        res[0] = generarTraslado(seed, cantCiudades);

        int i = 1;
        while (i < cantTraslados) {
            res[i] = generarTraslado(res[i-1].timestamp, cantCiudades);
            i++;
        }

        return res;
    }
}
