package com.proyect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionsToDatabases {
    public final static String user = "cipriano_12";
    public final static String password = "cipri12";

    public static Connection connectOracle() throws SQLException {
        String url = "jdbc:oracle:thin:@//192.168.1.25:1521/XE";
        return DriverManager.getConnection(url, user, password);
    }
}