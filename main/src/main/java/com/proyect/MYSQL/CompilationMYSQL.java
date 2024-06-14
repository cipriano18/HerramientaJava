package com.proyect.MYSQL;

import com.proyect.Connections.ConnectionsToDatabases;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class CompilationMYSQL {
    public void mainMYSQL(Scanner scanner) throws SQLException {
        SoccerPlayer soccerPlayer = new SoccerPlayer();
        Connection connection = ConnectionsToDatabases.connectMYSQUL();
        AutomaticMappingMYSQL automaticMappingMYSQL = new AutomaticMappingMYSQL(connection);
        soccerPlayer.setAutomaticMappingMYSQL(automaticMappingMYSQL);
            boolean exit = false;

            while (!exit) {
                System.out.println("Menú MySQL:");
                System.out.println("1. Insertar datos en la base de datos");
                System.out.println("2. Eliminar jugador de futbol de la tabla");
                System.out.println("3. Mostrar todos los jugaodres de futbol");
                System.out.println("4. Buscar jugador de futbol por ID");
                System.out.println("5. Actualizar un jugador de futbol");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el número del jugador:");
                        int number = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Ingrese el nombre del jugador:");
                        String name = scanner.nextLine();
                        System.out.println("Ingrese la edad del jugador:");
                        int edad = scanner.nextInt();
                        scanner.nextLine();
                        soccerPlayer.insert(name, number, edad);
                        break;
                    case 2:
                        System.out.println("Ingrese el id del jugador a eliminar:");
                        int idDelete = scanner.nextInt();
                        soccerPlayer.deleteById(idDelete);
                        break;

                    case 3:
                        soccerPlayer.readAll();
                        break;
                    case 4:
                        System.out.println("Ingrese el id a buscar");
                        int idSearch = scanner.nextInt();
                        soccerPlayer.readByID(idSearch);
                        break;
                    case 5:
                        System.out.println("Ingrese el id a actualizar:");
                        int Id = scanner.nextInt();
                        System.out.println("Ingrese el número del jugador:");
                        int newNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Ingrese el nombre del jugador:");
                        String newName = scanner.nextLine();
                        System.out.println("Ingrese la edad del jugador:");
                        int newAge = scanner.nextInt();
                        SoccerPlayer newSoccerPlayer = new SoccerPlayer(newNumber, newName, newAge);
                        newSoccerPlayer.setAutomaticMappingMYSQL(automaticMappingMYSQL);
                        soccerPlayer.update(newSoccerPlayer, Id);
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
