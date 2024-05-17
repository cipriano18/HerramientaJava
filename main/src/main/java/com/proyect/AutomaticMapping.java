package com.proyect;





import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.CallableStatement;



public class AutomaticMapping {
    private Connection connection;

    public AutomaticMapping(Connection connection) {
        this.connection = connection;
    }

    public void mapClassToTable(Object object) { 
        try {
            Class<?> clase = object.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase(); 

           
            if (!tableExists(nombreTabla)) {
                createTable(nombreTabla, clase);
            }

            
            Field[] fields = clase.getDeclaredFields();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ").append(nombreTabla).append(" (");
            for (Field field : fields) {
                field.setAccessible(true); // Permitir el acceso a campos privados
                queryBuilder.append(field.getName()).append(",");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Eliminar la última coma
            queryBuilder.append(") VALUES (");
            for (Field field : fields) {
                queryBuilder.append("?,");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Eliminar la última coma
            queryBuilder.append(")");

            // Preparar la declaración SQL
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            // Establecer los valores de los campos en la declaración SQL
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                statement.setObject(index++, value);
            }

            // Ejecutar la inserción
            statement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla + " correctamente.");
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al mapear la clase a la tabla: " + e.getMessage());
        }
    }
    public void eliminar(String nombreTabla, String nombreEstudiante) {
        try {
            String query = "DELETE FROM " + nombreTabla + " WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nombreEstudiante);
                int delete = statement.executeUpdate();
                System.out.println("Filas eliminadas: " + delete);
    
                if (delete > 0 ) {
                    System.out.println("Estudiante '" + nombreEstudiante + "' eliminado exitosamente." );
                } else {
                    System.out.println("El estudiante '" + nombreEstudiante + "' no existe en la tabla '" + nombreTabla + "'.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante de la tabla: " + e.getMessage());
        }
    }
    public void buscarPorNombre(String nombreTabla, String nombreEstudiante) {
        try {
            String query = "SELECT * FROM " + nombreTabla + " WHERE NAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nombreEstudiante);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("ID");
                        String nombre = resultSet.getString("NAME");
                        Student student = new Student(nombre, id); 
                        System.out.println(student.toString());
                    }
                }
                System.out.println("Estudiante no encontrado");
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre en la tabla: " + e.getMessage());
        }
    }
    private boolean tableExists(String tableName) throws SQLException {
        // Consulta para verificar si la tabla existe en la base de datos
        String query = "SELECT COUNT(*) FROM user_tables WHERE table_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName.toUpperCase()); // Oracle almacena los nombres de tabla en mayúsculas
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    public List<Map<String, Object>> recuperarDeTabla(String nombreTabla) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + nombreTabla;
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String nombreColumna = metaData.getColumnName(i);
                        Object valorColumna = resultSet.getObject(i);
                        fila.put(nombreColumna, valorColumna);
                    }
                    resultados.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar objetos de la tabla: " + e.getMessage());
        }
        return resultados;
    }

    private void createTable(String tableName, Class<?> clase) throws SQLException {
        // Construir la sentencia SQL para crear la tabla basada en los campos de la clase
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ").append(tableName).append(" (");
        Field[] fields = clase.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            // Aquí puedes definir la estructura de la tabla según los tipos de datos de los campos
            queryBuilder.append(fieldName).append(" ").append(getSqlType(fieldType)).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Eliminar la última coma
        queryBuilder.append(")");

        // Ejecutar la sentencia para crear la tabla
        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            statement.executeUpdate();
            System.out.println("Tabla " + tableName + " creada correctamente.");
        }
    }

    // Método auxiliar para mapear tipos de datos Java a tipos de datos SQL
    private String getSqlType(String javaType) {
        switch (javaType.toLowerCase()) {
            case "string":
                return "VARCHAR(255)";
            case "int":
                return "INT";
            // Agrega más casos según los tipos de datos que necesites
            default:
                return "VARCHAR(255)"; // Por defecto, mapeamos a VARCHAR
        }
    }
    public void insertarDatos(String nombreTabla, Class<?> clase, Object objeto) throws SQLException, IllegalAccessException {
        if (nombreTabla == null || clase == null) {
            throw new IllegalArgumentException("El nombre de la tabla y la clase no pueden ser nulos.");
        }
    
        StringBuilder query = new StringBuilder("INSERT INTO ").append(nombreTabla).append(" (");
        StringBuilder values = new StringBuilder("VALUES (");
    
        Field[] campos = clase.getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valor; // Aquí cambia object por objeto
            valor = campo.get(objeto);
            query.append(nombreCampo).append(", ");
            values.append("?, ");
        }
    
        query.delete(query.length() - 2, query.length());
        values.delete(values.length() - 2, values.length());
    
        query.append(") ");
        values.append(")");
    
        String insertQuery = query.toString() + values.toString();
    
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            int index = 1;
            for (Field campo : campos) {
                campo.setAccessible(true);
                Object valorCampo = campo.get(objeto);
                statement.setObject(index++, valorCampo);
            }
            statement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla + " correctamente.");
        }
    }
    
}