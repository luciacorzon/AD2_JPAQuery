package org.example;

import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JPAQuery {

    public static Scanner SCAN = new Scanner(System.in);

    public static void main(String[] args) {

        EntityManager em;
        if (args.length != 1) {
            // em = JPAUtil.getEntityManager();
            em = Persistence.createEntityManagerFactory("unidaddepersistencia").createEntityManager();
        } else {
//            em = JPAUtil.getEntityManager(args[0]);
            em = Persistence.createEntityManagerFactory(args[0]).createEntityManager();

        }

        System.out.println("Escribe la orden \"salir;\" para salir.");
        boolean salir = false;

        while (!salir) {

            System.out.print("Jakarta Persistence QL> ");
            StringBuilder sb = new StringBuilder(SCAN.nextLine().trim());
            while (!sb.toString().endsWith(";")) {
                sb.append(" ").append(SCAN.nextLine().trim());
            }
            String consulta = sb.substring(0, sb.length() - 1);
            if (!consulta.equalsIgnoreCase("salir")) { //
                if (consulta.isEmpty()) {
                    continue;
                }
                try {
                    if ("select".equalsIgnoreCase(consulta.trim().substring(0, 6))) {
                        // Consulta JPQL. La interfaz TypedQuery hereda de Query
                        // y permite la ejecución de consultas JPQL con la devolución de
                        // resultados tipados.
                        // TypedQuery<?> q = em.createQuery(consulta, Object.class);
                        List<?> resultado = em.createQuery(consulta).getResultList(); // Las wildcard permiten devolver cualquier tipo de objeto
                        if (!resultado.isEmpty()) {
                            int count = 0;
                            for (Object o : resultado) {
                                System.out.print(++count + " ");
                                mostrarResultados(o);
                            }
                        } else {
                            System.out.println("0 resultados de la consulta");
                        }
                    } else {
                        int i = em.createQuery(consulta).executeUpdate();
                        System.out.println(i + " elementos modificados");
                    }
                } catch (Exception e) {
                    System.out.println("Error al procesar  la consulta: " + e.getMessage());
                }
            } else {
                salir = true;
            }
        }
    }

    private static void mostrarResultados(Object resultado) {
        if (resultado == null) {
            System.out.print("NULL");
        } else if (resultado instanceof Object[] fila) {
            System.out.print("[");
            for (Object o : fila) {
                mostrarResultados(o);
            }
            System.out.print("]");
        } else if (resultado instanceof Long || resultado instanceof Double || resultado instanceof String) {
            System.out.print(resultado.getClass().getName() + ": " + resultado);
        } else {
            // ReflectionToStringBuilder es una clase de Apache Commons Lang que
            // permite la conversión de objetos a cadenas de texto.
//            System.out.print(ReflectionToStringBuilder.toString(resultado, ToStringStyle.SHORT_PREFIX_STYLE));
            System.out.print(resultado);
        }
        System.out.println();
    }
}