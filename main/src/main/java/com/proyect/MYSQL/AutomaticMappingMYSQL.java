package com.proyect.MYSQL;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutomaticMappingMYSQL {
    private Connection conexion;

    public AutomaticMappingMYSQL(Connection conexion) {
        this.conexion = conexion;
    }

   

    // CRUD INSERTAR ==========================================================================
    public void insertar(Object objeto) {
        try {
            Class<?> clase = objeto.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase(); // Suponemos que el nombre de la tabla es igual al nombre de la clase en minúsculas
            try {
                // Crear tabla si no existe
                crearTablaSiNoExiste(nombreTabla, clase);
            } catch (SQLException e) {
                System.err.println("Error al crear la tabla: " + e.getMessage());
            }

            // Insertar datos en la tabla
            insertarDatos(nombreTabla, clase, objeto);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al mapear la clase a la tabla: " + e.getMessage());
        }
    }
   
    private boolean tablaExiste(String nombreTabla) throws SQLException {
        String query="";
        if (conexion.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle")){
            query = "SELECT count(*) FROM user_tables WHERE table_name = ?";
        }
        if (conexion.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql")){
            query =  "SELECT count(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        }
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombreTabla.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

 private void crearTablaSiNoExiste(String nombreTabla, Class<?> clase) throws SQLException {
    if (tablaExiste(nombreTabla)) {
        System.out.println("La tabla " + nombreTabla + " ya existe.");
        return;
    }

    StringBuilder query = new StringBuilder("CREATE TABLE ")
            .append(nombreTabla)
            .append(" (");

    query.append("id INT AUTO_INCREMENT PRIMARY KEY, "); // Definir campo autoincrementado

    Field[] campos = clase.getDeclaredFields();
    for (Field campo : campos) {
        campo.setAccessible(true); // Permitir acceso a campos privados
        String nombreCampo = campo.getName();
        String tipoDato = obtenerTipoDato(campo.getType());
        query.append(nombreCampo).append(" ").append(tipoDato);
        query.append(", ");
    }

    // Eliminar la coma y el espacio extra al final de la definición de la tabla
    query.delete(query.length() - 2, query.length());
    query.append(")");

    try (PreparedStatement statement = conexion.prepareStatement(query.toString())) {
        statement.executeUpdate();
        System.out.println("Tabla " + nombreTabla + " creada correctamente.");
    }
}
    private String obtenerTipoDato(Class<?> tipo) {
        if (tipo == String.class) {
            return "VARCHAR(255)";
        } else if (tipo == int.class || tipo == Integer.class) {
            return "INT";
        } else if (tipo == double.class || tipo == Double.class) {
            return "DOUBLE";
        } else if (tipo == float.class || tipo == Float.class) {
            return "FLOAT";
        } else if (tipo == boolean.class || tipo == Boolean.class) {
            return "BOOLEAN";
        } else {
            return "VARCHAR(255)"; // Por defecto, se considera como String
        }
    }
     private void insertarDatos(String nombreTabla, Class<?> clase, Object objeto) throws SQLException, IllegalAccessException {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(nombreTabla).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");

        Field[] campos = clase.getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valorCampo = campo.get(objeto);

            query.append(nombreCampo).append(", ");
            values.append("'").append(valorCampo).append("', ");
        }

        // Eliminar la coma y el espacio extra al final de las listas de campos y valores
        query.delete(query.length() - 2, query.length());
        values.delete(values.length() - 2, values.length());

        query.append(") ");
        values.append(")");

        String insertQuery = query.toString() + values.toString();

        try (PreparedStatement statement = conexion.prepareStatement(insertQuery)) {
            statement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla + " correctamente.");
        }
    }
}