package com.proyect.MYSQL;

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

public class AutomaticMappingMYSQL {
    private Connection connection;

    public AutomaticMappingMYSQL(Connection connection) {
        this.connection = connection;
    }

    public void insertar(Object objeto) {
        try {
            Class<?> clase = objeto.getClass();
            String nombreTabla = clase.getSimpleName().toLowerCase();
            try {

                crearTablaSiNoExiste(nombreTabla, clase);
            } catch (SQLException e) {
                System.err.println("Error al crear la tabla: " + e.getMessage());
            }
            insertarDatos(nombreTabla, clase, objeto);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error al mapear la clase a la tabla: " + e.getMessage());
        }
    }

    private boolean tablaExiste(String nombreTabla) throws SQLException {
        String query = "";
        if (connection.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle")) {
            query = "SELECT count(*) FROM user_tables WHERE table_name = ?";
        }
        if (connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql")) {
            query = "SELECT count(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
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

        query.append("id INT AUTO_INCREMENT PRIMARY KEY, ");

        Field[] campos = clase.getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            String tipoDato = obtenerTipoDato(campo.getType());
            query.append(nombreCampo).append(" ").append(tipoDato);
            query.append(", ");
        }
        query.delete(query.length() - 2, query.length());
        query.append(")");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
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
            return "VARCHAR(255)";
        }
    }

    private void insertarDatos(String nombreTabla, Class<?> clase, Object objeto)
            throws SQLException, IllegalAccessException {
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

        query.delete(query.length() - 2, query.length());
        values.delete(values.length() - 2, values.length());

        query.append(") ");
        values.append(")");

        String insertQuery = query.toString() + values.toString();
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.executeUpdate();
            System.out.println("Datos insertados en la tabla " + nombreTabla + " correctamente.");
        }
    }

    public void update(String nameClass, int id, Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ").append(nameClass).append(" SET ");

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName();
                Object value = field.get(object);

                queryBuilder.append(columnName).append(" = ");
                if (obtenerTipoDato(field.getType()).equalsIgnoreCase("VARCHAR(255)")) {
                    queryBuilder.append("'").append(value).append("' ,");
                } else {
                    queryBuilder.append(value).append(" ,");
                }
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(" WHERE id = ?");

            try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }

            System.out.println("Exito al actualizarlo");
        } catch (Exception e) {
            System.out.println("Error al actualizar la tabla " + e.getMessage());
        }
    }

    public List<String> readByID(String nameClass, int id) {
        List<String> result = new ArrayList<>();

        try (Connection connection = ConnectionsToDatabases.connectMYSQUL()) {
            String query = "SELECT * FROM " + nameClass + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        result.add("id: " + resultSet.getInt("id"));
                        result.add("number: " + resultSet.getInt("number"));
                        result.add("name: " + resultSet.getString("name"));
                        result.add("edad: " + resultSet.getInt("edad"));
                        result.add("automaticMappingMYSQL: " + resultSet.getString("automaticMappingMYSQL"));
                        result.add("nameClass: " + resultSet.getString("nameClass"));
                    } else {
                        result.add("EL ID NO EXISTE");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Map<String, Object>> readAll(String tableName) {
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

    public void delete(String nameClass, int id) {
        try {
            String query = "DELETE FROM " + nameClass + " where id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int delete = preparedStatement.executeUpdate();
                if (delete > 0) {
                    System.out.println("El futbolista fue eliminado");
                } else {
                    System.out.println("El futbolista no existe");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}