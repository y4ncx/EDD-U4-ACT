gpackage com.y4ncx.actividad;

import java.util.Map;

public class Utilidades {
    public static void imprimirMapa(Map<?, ?> mapa) {
        mapa.forEach((k, v) -> System.out.println(k + " - " + v));
    }
}
