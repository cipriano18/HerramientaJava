package com.proyect.MYSQL;

import com.proyect.Connections.ConnectionsToDatabases;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class CompilationMYSQL {
public void mainMYSQL() throws SQLException {
       SoccerPlayer soccerPlayer=new SoccerPlayer();
        Connection connection = ConnectionsToDatabases.connectMYSQUL();
       AutomaticMappingMYSQL automaticMappingMYSQL= new AutomaticMappingMYSQL(connection);
       soccerPlayer.setAutomaticMappingMYSQL(automaticMappingMYSQL);

       try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            while (!exit) {
                System.out.println("Menú Oracle:");
                System.out.println("1. Insertar datos en la base de datos");
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
                    soccerPlayer.insert(name,number,edad);
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
