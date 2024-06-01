package com.proyect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConnectionsToDatabases {
   private final static String password = "cipri12";
   private final static String databaseName = "prueba"; 
   private final static String host = "127.0.0.1";
   private final static int port = 27017;
    public static Connection connectOracle() throws SQLException {
        String url = "jdbc:oracle:thin:@//192.168.1.25:1521/XE";
        return DriverManager.getConnection(url, user, password);
    }
   public static MongoDatabase connectMongo() {
        MongoCredential credential = MongoCredential.createCredential(user, databaseName, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                .credential(credential)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("Cipriano");
    }
}