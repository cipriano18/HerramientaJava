package com.proyect.MongoBD;

import com.mongodb.client.MongoDatabase;

import java.util.Scanner;

public class CompilationMongoBD {
    public void mainMongoBD() {
        MongoDatabase conexionMongo = ConnectionsToDatabases.conectarMongoDB();
        AutomaticMappingMongo mapeadorMongo = new AutomaticMappingMongo(conexionMongo);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        person person = new person();

        while (!exit) {
            System.out.println("Menú MongoDB:");
            System.out.println("1. Insertar datos en la base de datos");
            System.out.println("2. Eliminar documento de la colección");
            System.out.println("3. Mostrar todos los documentos");
            System.out.println("4. Buscar documento por ID");
            System.out.println("5. Actualizar un documento");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del objeto: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese la edad del objeto: ");
                    int edad = scanner.nextInt();
                    scanner.nextLine();
                    person nuevoPerson = new person(nombre, edad);
                    nuevoPerson.insert(nuevoPerson, mapeadorMongo);
                    break;
                case 2:
                    System.out.print("Ingrese el ID del documento a eliminar: ");
                    String idEliminar = scanner.nextLine();
                    person.delete(idEliminar, mapeadorMongo);
                    break;
                case 3:
                    person.read(mapeadorMongo);
                    break;
                case 4:
                    System.out.print("Ingrese el ID del documento a buscar: ");
                    String idBuscar = scanner.nextLine();
                    person.readById(idBuscar, mapeadorMongo);
                    break;
                case 5:
                    System.out.print("Ingrese el ID del documento a actualizar: ");
                    String idActualizar = scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre del objeto: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese la nueva edad del objeto: ");
                    int nuevaEdad = scanner.nextInt();
                    scanner.nextLine();
                    person actualizarPerson = new person(nuevoNombre, nuevaEdad);
                    person.update(idActualizar, actualizarPerson, mapeadorMongo);;
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
