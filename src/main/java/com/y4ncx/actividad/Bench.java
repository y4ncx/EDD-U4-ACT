package com.y4ncx.actividad;

import java.util.*;

public class Bench {
    public static void compararRendimiento() {
        int n = 100000;
        List<Integer> lista = new ArrayList<>();
        Set<Integer> conjunto = new HashSet<>();
        Map<Integer,Integer> hashmap = new HashMap<>();
        Map<Integer,Integer> treemap = new TreeMap<>();

        long t1 = System.nanoTime();
        for (int i = 0; i < n; i++) lista.add(i);
        long t2 = System.nanoTime();

        long t3 = System.nanoTime();
        lista.contains(n - 1);
        long t4 = System.nanoTime();

        for (int i = 0; i < n; i++) conjunto.add(i);
        long t5 = System.nanoTime();
        conjunto.contains(n - 1);
        long t6 = System.nanoTime();

        for (int i = 0; i < n; i++) {
            hashmap.put(i, i);
            treemap.put(i, i);
        }
        long t7 = System.nanoTime();
        hashmap.get(n - 1);
        long t8 = System.nanoTime();
        treemap.get(n - 1);
        long t9 = System.nanoTime();

        System.out.println("Operacion | Coleccion | Tiempo (ns)");
        System.out.println("Insercion | List      | " + (t2 - t1));
        System.out.println("Busqueda  | List      | " + (t4 - t3));
        System.out.println("Busqueda  | Set       | " + (t6 - t5));
        System.out.println("Lookup    | HashMap   | " + (t8 - t7));
        System.out.println("Lookup    | TreeMap   | " + (t9 - t8));
    }
}
