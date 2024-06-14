package com.proyect;

import java.sql.SQLException;
import java.util.Scanner;
import com.proyect.MongoBD.CompilationMongoBD;
import com.proyect.Oracle.CompilationOracle;
import com.proyect.MYSQL.CompilationMYSQL;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Ingrese 1 para utilizar la base de datos Oracle");
            System.out.println("Ingrese 2 para utilizar la base de datos MongoDB");
            System.out.println("Ingrese 3 para utilizar la base de datos MySQL");
            System.out.println("Ingrese 4 para salir del programa");
            System.out.println("Ingrese una opcion");
            int option = 0;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.next();
                continue;
            }

            switch (option) {
                case 1:
                    CompilationOracle compilationOracle = new CompilationOracle();
                    compilationOracle.mainOracle(scanner);
                    break;
                case 2:
                    CompilationMongoBD compilationMongoBD = new CompilationMongoBD();
                    compilationMongoBD.mainMongoBD(scanner);
                    break;
                case 3:
                    CompilationMYSQL compilationMySQL = new CompilationMYSQL();
                    compilationMySQL.mainMYSQL(scanner);
                    break;
                case 4:
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, ingrese 1, 2, 3 o 4.");
                    break;
            }

            if (!exit) {
                System.out.println("¿Desea utilizar otra base de datos? (si/no)");
                String respuesta = scanner.nextLine().toLowerCase();
                if (!respuesta.equals("si")) {
                    exit = true;
                    System.out.println("Saliendo del programa...");
                }
            }
        }

        scanner.close();
    }
}