package com.proyect.Connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConnectionsToDatabases {
   private final static String user = "cipriano_12";
   private final static String password = "cipri12";
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String MONGO_DATABASE_NAME = "Proyect_II";
    private final static String userMYSQL ="root";
    private final static String passwordMYSQL ="cipri12@+";
      public static Connection connectOracle() throws SQLException {
        String url = "jdbc:oracle:thin:@//192.168.1.25:1521/XE";
        return DriverManager.getConnection(url, user, password);
    }
public static MongoDatabase conectarMongoDB() {
        ConnectionString connectionString = new ConnectionString(MONGO_URI);
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient.getDatabase(MONGO_DATABASE_NAME);
    }
public static Connection connectMYSQUL() throws SQLException{
 String urlMYSQL = "jdbc:mysql://localhost:3306/world"; 
    return DriverManager.getConnection(urlMYSQL, userMYSQL, passwordMYSQL);
}
}