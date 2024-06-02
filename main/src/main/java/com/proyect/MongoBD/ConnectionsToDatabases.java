package com.proyect.MongoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConnectionsToDatabases {
    private final static String user = "Stiff_Cipri";
    private final static String password = "stiff1824";

    // Parámetros de conexión para MongoDB
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String MONGO_DATABASE_NAME = "Proyect_II";

    public static Connection connectOracle() throws SQLException {
        String url = "jdbc:oracle:thin:@//localhost:1521/XE";
        return DriverManager.getConnection(url, user, password);
    }

    public static MongoDatabase conectarMongoDB() {
        ConnectionString connectionString = new ConnectionString(MONGO_URI);
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient.getDatabase(MONGO_DATABASE_NAME);
    }
}