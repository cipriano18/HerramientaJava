package com.proyect;

import java.sql.SQLException;

import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {
        String id = "665bb9086d7d163cdb59312f";
        person n = new person("CIPRIANO", 53);
        MongoDatabase conexionMongo = ConnectionsToDatabases.conectarMongoDB();
        AutomaticMappingMongo mapeadorMongo = new AutomaticMappingMongo(conexionMongo);
        person nuevoPerson = new person("cipriano", 2);
        nuevoPerson.insert(nuevoPerson, mapeadorMongo);
        nuevoPerson.update(id, n, mapeadorMongo);
    }
    // Connection conexionOracle = ConnectionsToDatabases.connectOracle();
    // AutomaticMapping automaticMapping = new AutomaticMapping(conexionOracle);

    // Student student = new Student();
    // student.setAutomaticMapping(automaticMapping);

    // try (Scanner scanner = new Scanner(System.in)) {
    // boolean exit = false;

    // while (!exit) {
    // System.out.println("Menú:");
    // System.out.println("1. Insertar datos en la base de datos");
    // System.out.println("2. Eliminar estudiante de la tabla");
    // System.out.println("3. Mostrar todos los estudiantes");
    // System.out.println("4. Buscar estudiante por ID");
    // System.out.println("5. Actualizar un estudiante");
    // System.out.println("6. Salir");
    // System.out.print("Seleccione una opción: ");
    // int opcion = scanner.nextInt();

    // switch (opcion) {
    // case 1:
    // System.out.println("Ingrese el nombre del estudiante:");
    // String nombre = scanner.nextLine();
    // nombre = scanner.nextLine();
    // System.out.println("Ingrese el número de cédula del estudiante:");
    // int id = scanner.nextInt();
    // student.insert(nombre, id);
    // break;
    // case 2:
    // System.out.print("Ingrese el ID a eliminar: ");
    // int idEliminar = scanner.nextInt();
    // student.delete(idEliminar);
    // break;
    // case 3:
    // System.out.print("Ingrese el ID a buscar: ");
    // int idBuscar = scanner.nextInt();
    // student.searchById(idBuscar);
    // break;
    // case 4:
    // student.retrieveAll();
    // break;
    // case 5:
    // System.out.print("Ingrese el ID a actualizar: ");
    // int idActualizar = scanner.nextInt();
    // student.update(idActualizar);
    // break;
    // case 6:
    // System.out.println("Saliendo...");
    // exit = true;
    // break;
    // default:
    // System.out.println("Opción no válida");
    // break;
    // }
    // }
    // }
}
