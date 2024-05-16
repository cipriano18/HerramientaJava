package com.proyect;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutomaticMapping {
    private Connection connection;

    public AutomaticMapping(Connection connection) {
        this.connection = connection;
    }

    public void mapClassToTable(Object object) { 
        try {
            Class<?> clase = object.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase(); // Suponemos que el nombre de la tabla es igual al nombre de la clase en minúsculas

            // Verificar si la tabla ya existe en la base de datos
            if (!tableExists(nombreTabla)) {
                createTable(nombreTabla, clase);
            }

            // Construir la sentencia SQL de inserción
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
}