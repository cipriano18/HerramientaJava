package com.proyect;

import java.sql.SQLException;

import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {

            MongoDatabase mongoClient = ConnectionsToDatabases.connectMongo();
            AutomaticMappingMongo automaticMappingMongo =new AutomaticMappingMongo(mongoClient);
            person person = new person(automaticMappingMongo);
            person.setName("cipriano");
            person.setEdad(19);
            person.insert();
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
}