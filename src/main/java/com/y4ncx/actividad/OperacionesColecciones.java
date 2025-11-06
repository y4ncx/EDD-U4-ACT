package com.y4ncx.actividad;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperacionesColecciones {

    public static List<Double> transformarRedondeo(List<Double> datos) {
        return datos.stream()
                .map(n -> Math.round(n * 100.0) / 100.0)
                .collect(Collectors.toList());
    }

    public static List<Double> filtrarPositivos(List<Double> datos) {
        return datos.stream()
                .filter(n -> n > 0)
                .collect(Collectors.toList());
    }

    public static Map<Double, Long> contarFrecuencias(List<Double> datos) {
        return datos.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
    }
}
