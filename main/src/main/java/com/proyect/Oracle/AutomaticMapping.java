package com.proyect.Oracle;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyect.Connections.ConnectionsToDatabases;

public class AutomaticMapping {
    private Connection connection;
    private String tableName;

    public AutomaticMapping(String tableName) {
        this.tableName = tableName;
    }

    public AutomaticMapping(Connection connection) {
        this.connection = connection;
    }

    public void mapClassToTable(Object object) {
        try {
            Class<?> clazz = object.getClass();
            String tableName = clazz.getSimpleName().toLowerCase();

            if (!tableExists(tableName)) {
                createTable(tableName, clazz);
            }

            Field[] fields = clazz.getDeclaredFields();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ").append(tableName).append(" (");
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("automaticMapping") && !field.getName().equals("nameClass")) {
                    queryBuilder.append(field.getName()).append(",");
                }
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(") VALUES (");
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("automaticMapping") && !field.getName().equals("nameClass")) {
                    queryBuilder.append("?,");
                }
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(")");

            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("automaticMapping") && !field.getName().equals("nameClass")) {
                    Object value = field.get(object);
                    statement.setObject(index++, value);
                }
            }

            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al mapear la clase a la tabla: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM user_tables WHERE table_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private void createTable(String tableName, Class<?> clazz) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ").append(tableName).append(" (");
        queryBuilder.append("id NUMBER GENERATED BY DEFAULT AS IDENTITY, ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            queryBuilder.append(fieldName).append(" ").append(getSqlType(fieldType)).append(",");
        }
        queryBuilder.append("PRIMARY KEY (id)");
        queryBuilder.append(")");

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            statement.executeUpdate();
            System.out.println("Tabla " + tableName + " creada correctamente.");
        }
    }

    private String getSqlType(String javaType) {
        switch (javaType.toLowerCase()) {
            case "string":
                return "VARCHAR(255)";
            case "int":
                return "INT";
            default:
                return "VARCHAR(255)";
        }
    }

    public List<Map<String, Object>> recuperarDeTabla(String tableName) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + tableName;
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

    public List<Map<String, Object>> searchById(String className, int id) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = ConnectionsToDatabases.connectOracle()) {
            String query = "SELECT * FROM " + className + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("ID", resultSet.getInt("ID"));
                        row.put("NAME", resultSet.getString("NAME"));
                        row.put("AGE", resultSet.getInt("AGE"));
                        results.add(row);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public void deleteById(String className, int id) {
        try (Connection connection = ConnectionsToDatabases.connectOracle()) {
            String query = "DELETE FROM " + className + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                int delete = statement.executeUpdate();

                if (delete > 0) {
                    System.out.println("Estudiante con ID " + id + " eliminado exitosamente.");
                } else {
                    System.out.println("El estudiante con ID " + id + " no existe en la tabla '" + tableName + "'.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar estudiante de la tabla: " + e.getMessage());
        }
    }

    public void updateTable(String tableName, int id, Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ").append(tableName).append(" SET ");

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName();
                queryBuilder.append(columnName).append(" = ?, ");
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(" WHERE id = ?");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                int index = 1;
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    statement.setObject(index++, value);
                }
                statement.setInt(index, id);
                statement.executeUpdate();
                
            }
            System.out.println("Exito al actualizarlo");
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
        }
    }
}