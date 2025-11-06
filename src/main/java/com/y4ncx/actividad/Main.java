package com.y4ncx.actividad;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Path entrada;
        Path salidaEstadisticas;
        Path salidaOrdenado;

        if (args.length < 3) {
            System.out.println("⚠️ Usando archivos por defecto para pruebas locales.");
            entrada = Paths.get("src/main/resources/numeros.txt");
            salidaEstadisticas = Paths.get("estadisticas.txt");
            salidaOrdenado = Paths.get("ordenado.txt");
        } else {
            entrada = Paths.get(args[0]);
            salidaEstadisticas = Paths.get(args[1]);
            salidaOrdenado = Paths.get(args[2]);
        }

        try {
            if (!Files.exists(entrada)) {
                System.err.println("❌ Archivo de entrada no encontrado: " + entrada.toAbsolutePath());
                System.exit(2);
            }

            // === Lectura y análisis numérico ===
            List<Double> datos = AnalizadorNumeros.LeerNumerosDesdeArchivo(entrada);
            if (datos.isEmpty()) {
                System.err.println("⚠️ El archivo no contiene números válidos.");
                System.exit(2);
            }

            // Generar estadísticas
            String resumen = AnalizadorNumeros.generarResumen(datos);
            Files.writeString(salidaEstadisticas, resumen);

            // Ordenar y guardar
            List<Double> ordenado = AnalizadorNumeros.ordenar(datos, true);
            AnalizadorNumeros.guardarListaEnArchivo(ordenado, salidaOrdenado);

            System.out.println("✅ Análisis completado correctamente.\n");
            System.out.println(resumen);

            // === Parte A: Árboles ===
            System.out.println("🌳 Árboles:");

            TreeSet<Double> arbolBalanceado = new TreeSet<>(datos);
            System.out.println("Números en TreeSet (ordenados): " + arbolBalanceado);

            PriorityQueue<Double> colaPrioridad = new PriorityQueue<>(Collections.reverseOrder());
            colaPrioridad.addAll(datos);
            System.out.println("Atendiendo números según prioridad (mayor primero):");
            while (!colaPrioridad.isEmpty()) {
                System.out.print(colaPrioridad.poll() + " ");
            }
            System.out.println("\n");

            // === Parte B: Grafos ===
            System.out.println("🗺️ Grafos:");

            SimpleDirectedWeightedGraph<Double, DefaultWeightedEdge> grafo =
                    new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

            for (Double d : datos) grafo.addVertex(d);

            // Conectar nodos si diferencia <= 10
            for (Double a : datos) {
                for (Double b : datos) {
                    if (!a.equals(b) && Math.abs(a - b) <= 10) {
                        DefaultWeightedEdge edge = grafo.addEdge(a, b);
                        if (edge != null) grafo.setEdgeWeight(edge, Math.abs(a - b));
                    }
                }
            }

            // Dijkstra: ruta más corta entre min y max con verificación de null
            Double min = Collections.min(datos);
            Double max = Collections.max(datos);
            DijkstraShortestPath<Double, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafo);
            var path = dijkstra.getPath(min, max);
            if (path != null) {
                System.out.println("Ruta más corta " + min + " -> " + max + ": " + path.getVertexList());
            } else {
                System.out.println("No hay ruta entre " + min + " y " + max);
            }

            // Detección de ciclos
            CycleDetector<Double, DefaultWeightedEdge> detector = new CycleDetector<>(grafo);
            System.out.println("¿El grafo tiene ciclos? " + detector.detectCycles());

            // === Operaciones CEA existentes ===
            System.out.println("\n🔹 Operaciones adicionales de colecciones:");
            List<Double> redondeados = OperacionesColecciones.transformarRedondeo(datos);
            System.out.println("Ejemplo (redondeados): " + redondeados.subList(0, Math.min(5, redondeados.size())));

            List<Double> positivos = OperacionesColecciones.filtrarPositivos(datos);
            System.out.println("Ejemplo (positivos): " + positivos.subList(0, Math.min(5, positivos.size())));

            Map<Double, Long> frecuencias = OperacionesColecciones.contarFrecuencias(redondeados);
            System.out.println("\nFrecuencias (valor - repeticiones):");
            Utilidades.imprimirMapa(frecuencias);

            System.out.println("\n⚙️ Comparando rendimiento de colecciones...");
            Bench.compararRendimiento();

            System.out.println("\n✅ Programa finalizado con éxito.");

        } catch (IOException e) {
            System.err.println("💥 Error de entrada/salida: " + e.getMessage());
            System.exit(3);
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Formato de número inválido en el archivo: " + e.getMessage());
            System.exit(4);
        } catch (Exception e) {
            System.err.println("🚨 Error inesperado: " + e.getMessage());
            System.exit(5);
        }
    }
}
