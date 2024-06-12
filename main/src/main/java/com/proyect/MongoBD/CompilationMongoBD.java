package com.proyect.MongoBD;

import com.mongodb.client.MongoDatabase;
import com.proyect.Connections.ConnectionsToDatabases;

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
            System.out.println("2. Eliminar persona");
            System.out.println("3. Mostrar todos las persona");
            System.out.println("4. Buscar persona por ID");
            System.out.println("5. Actualizar una persona");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de al persona: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese la edad de la persona: ");
                    int edad = scanner.nextInt();
                    scanner.nextLine();
                    person nuevoPerson = new person(nombre, edad);
                    nuevoPerson.insert(nuevoPerson, mapeadorMongo);
                    break;
                case 2:
                    System.out.print("Ingrese el ID de la persona a eliminar: ");
                    String idEliminar = scanner.nextLine();
                    person.delete(idEliminar, mapeadorMongo);
                    break;
                case 3:
                    person.read(mapeadorMongo);
                    break;
                case 4:
                    System.out.print("Ingrese el ID de la persona a buscar: ");
                    String idBuscar = scanner.nextLine();
                    person.readById(idBuscar, mapeadorMongo);
                    break;
                case 5:
                    System.out.print("Ingrese el ID de la persona a actualizar: ");
                    String idActualizar = scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre de la persona: ");
                    String nuevoNombre = scanner.nextLine();
                    System.out.print("Ingrese la nueva edad de la persona: ");
                    int nuevaEdad = scanner.nextInt();
                    scanner.nextLine();
                    person actualizarPerson = new person(nuevoNombre, nuevaEdad);
                    person.update(idActualizar, actualizarPerson, mapeadorMongo);
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
