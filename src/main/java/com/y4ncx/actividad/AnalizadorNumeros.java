package com.y4ncx.actividad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AnalizadorNumeros {

    public static List<Double> LeerNumerosDesdeArchivo(Path path) throws IOException {
        List<Double> resultados = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split("[\\s,;]+");
            for (String token : tokens) {
                token = token.trim();
                if (token.isEmpty()) continue;
                try {
                    String normalized = token.replace(',', '.');
                    double d = Double.parseDouble(normalized);
                    resultados.add(d);
                } catch (NumberFormatException e) {
                    System.err.println("Ignorado token no numérico en línea " + (i+1) + ": '" + token + "'");
                }
            }
        }
        return resultados;
    }

    public static OptionalDouble calcularMin(List<Double> datos){
        return datos.stream().mapToDouble(Double::doubleValue).min();
    }

    public static OptionalDouble calcularMax(List<Double> datos){
        return datos.stream().mapToDouble(Double::doubleValue).max();
    }

    public static OptionalDouble calcularPromedio(List<Double> datos){
        return datos.stream().mapToDouble(Double::doubleValue).average();
    }

    public static OptionalDouble calcularMediana(List<Double> datos){
        if (datos.isEmpty()) return OptionalDouble.empty();
        List<Double> sorted = datos.stream().sorted().collect(Collectors.toList());
        int n = sorted.size();
        if (n % 2 == 1) return OptionalDouble.of(sorted.get(n / 2));
        else return OptionalDouble.of((sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0);
    }

    public static List<Double> ordenar(List<Double> datos, boolean ascendente) {
        return datos.stream()
                .sorted(ascendente ? Comparator.naturalOrder() : Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static void guardarListaEnArchivo(List<Double> datos, Path destino) throws IOException {
        List<String> lineas = datos.stream()
                .map(d -> String.format(Locale.ROOT, "%s", d))
                .collect(Collectors.toList());
        Files.write(destino, lineas);
    }

    public static String generarResumen(List<Double> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Analizador de archivos numericos\n");
        sb.append("-------------------------------\n");
        sb.append("Total de valores: ").append(datos.size()).append("\n");
        calcularMin(datos).ifPresent(min -> sb.append("Minimo: ").append(min).append("\n"));
        calcularMax(datos).ifPresent(max -> sb.append("Maximo: ").append(max).append("\n"));
        calcularPromedio(datos).ifPresent(avg -> sb.append("Promedio: ").append(String.format(Locale.ROOT, "%.4f", avg)).append("\n"));
        calcularMediana(datos).ifPresent(med -> sb.append("Mediana: ").append(String.format(Locale.ROOT, "%.4f", med)).append("\n"));
        List<Double> sorted = ordenar(datos, true);
        sb.append("\nPrimeros 10 valores ordenados:\n");
        sorted.stream().limit(10).forEach(v -> sb.append(v).append("\n"));
        sb.append("\n");
        return sb.toString();
    }
}