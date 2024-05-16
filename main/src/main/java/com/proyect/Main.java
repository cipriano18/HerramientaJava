package com.proyect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Establecer conexión a la base de datos Oracle
            Connection conexionOracle = ConnectionsToDatabases.connectOracle();

            // Crear una instancia de la clase AutomaticMapping pasando la conexión como argumento
            AutomaticMapping automaticMapping = new AutomaticMapping(conexionOracle);

            // Crear una instancia de la clase que deseas mapear a la tabla
            // Por ejemplo, si tienes una clase Student:
            Student student = new Student("Stiff", 23345434);

            // Mapear la clase a la tabla en la base de datos Oracle
            automaticMapping.mapClassToTable(student);

            // Cerrar la conexión a la base de datos
            conexionOracle.close();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
