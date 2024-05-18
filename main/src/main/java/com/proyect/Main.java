package com.proyect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        try (Scanner scanner = new Scanner(System.in)) {
            Connection conexionOracle = ConnectionsToDatabases.connectOracle();
            AutomaticMapping automaticMapping = new AutomaticMapping(conexionOracle);
            String nombreTabla = Student.class.getSimpleName();
            boolean salir = false;
            while (!salir) {
                System.out.println("Menú:");
                System.out.println("1. Insertar datos en la base de datos");
                System.out.println("2. Recuperar datos de la tabla");
                System.out.println("3. Eliminar estudiante de la tabla");
                System.out.println("4. Mostrar todos los estudiantes");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el nombre del estudiante:");
                        String name = scanner.nextLine();
                        System.out.println("Ingrese la edad del estudiante:");
                        int age = scanner.nextInt();
                        Student newStudent = new Student(name, age);

                        automaticMapping.mapClassToTable(newStudent);
                        break;
                    case 2:
                        System.out.println("Ingrese el ID a buscar:");
                        int id = scanner.nextInt();
                        Map<String, Object> result = automaticMapping.getByID(nombreTabla, id);
                        if (!result.isEmpty()) {
                            System.out.println("Resultados encontrados:");
                            for (Map.Entry<String, Object> entry : result.entrySet()) {
                                System.out.println(entry.getKey() + ": " + entry.getValue());
                            }
                        } else {
                            System.out.println("No se encontraron resultados para el ID proporcionado.");
                        }

                        break;
                    case 3:
                        System.out.println("Ingrese el id a eliminar:");
                        String nombreEliminar = scanner.nextLine();
                        automaticMapping.eliminar(nombreTabla, nombreEliminar);
                        break;
                    case 4:
                        List<Map<String, Object>> resultados = automaticMapping.recuperarDeTabla(nombreTabla);
                        for (Map<String, Object> fila : resultados) {
                            System.out.println(fila);
                        }
                        break;
                    case 5:
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                        break;
                }
            }
            conexionOracle.close();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
