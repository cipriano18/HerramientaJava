package com.proyect.Oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.proyect.MongoBD.ConnectionsToDatabases;

public class CompilationOracle {
    public void mainOracle() throws SQLException {
        Connection conexionOracle = ConnectionsToDatabases.connectOracle();
        AutomaticMapping automaticMapping = new AutomaticMapping(conexionOracle);

        Student student = new Student();
        student.setAutomaticMapping(automaticMapping);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            while (!exit) {
                System.out.println("Menú Oracle:");
                System.out.println("1. Insertar datos en la base de datos");
                System.out.println("2. Eliminar estudiante de la tabla");
                System.out.println("3. Mostrar todos los estudiantes");
                System.out.println("4. Buscar estudiante por ID");
                System.out.println("5. Actualizar un estudiante");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el nombre del estudiante:");
                        String nombre = scanner.nextLine();
                        System.out.println("Ingrese la edad del estudiante:");
                        int id = scanner.nextInt();
                        student.insert(nombre, id);
                        break;
                    case 2:
                        System.out.print("Ingrese el ID a eliminar: ");
                        int idEliminar = scanner.nextInt();
                        student.delete(idEliminar);
                        break;
                    case 3:
                        student.retrieveAll();
                        break;
                    case 4:
                        System.out.print("Ingrese el ID a buscar: ");
                        int idBuscar = scanner.nextInt();
                        student.searchById(idBuscar);
                        break;
                    case 5:
                    System.out.print("Ingrese el ID a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre del estudiante: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese la nueva edad del estudiante: ");
                    int nuevaEdad = scanner.nextInt();
                    Student student2 = new Student(nuevoNombre, nuevaEdad);
                    student.update(idActualizar, student2);
                    break;
                    case 6:
                        System.out.println("Saliendo...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }
        }
    }
}
