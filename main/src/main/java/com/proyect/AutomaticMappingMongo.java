package com.proyect;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

public class AutomaticMappingMongo {
    private MongoDatabase mongoDatabase;
    private Class<?> clase;

    public AutomaticMappingMongo(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public void insert(Object object, String nameClass) {
        try {
            Class<?> clase = object.getClass();
            MongoCollection<Document> coleccion = mongoDatabase.getCollection(nameClass);
            Document document = convertirAJson(object, clase);
            coleccion.insertOne(document);
            System.err.println("Documento insertado en la colecion");

        } catch (Exception e) {
            System.out.println("Error al insertar un objeto a mongoCompas");
        }
    }

    private Document convertirAJson(Object object, Class<?> clase) throws IllegalAccessException {
        Document doc = new Document();
        Field[] campos = clase.getDeclaredFields();
        String idFieldName = clase.getSimpleName().toLowerCase() + "_id";

        for (Field campo : campos) {
            campo.setAccessible(true);
            String nombreCampo = campo.getName();
            Object valorCampo = campo.get(object);
            doc.append(nombreCampo, valorCampo);
        }
        return doc;
    }

    public void readById(String id, String collectName) {

        MongoCollection<Document> collection = mongoDatabase.getCollection(collectName);

        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            System.out.println(doc.toJson());
        } else {
            System.out.println("No se encontraron documentos.");
        }
    }

    public void read(String collectName) {
        MongoCollection<Document> collections = mongoDatabase.getCollection(collectName);

        for (Document doc : collections.find()) {
            System.out.println(doc.toJson());
        }
    }

    public void delete(String id, String collectName) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectName);
        Bson filter = Filters.eq("_id", new ObjectId(id));
        
        try {
            collection.deleteOne(filter);
            System.out.println("Documento eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar el documento: " + e.getMessage());
        }
    }
    

    public void update(String id, Object object, String nameClass) {
        try {
            Class<?> clase = object.getClass();
            MongoCollection<Document> coleccion = mongoDatabase.getCollection(nameClass);
            Document nuevoDocumento = convertirAJson(object, clase);
            Bson filtro = Filters.eq("_id", new ObjectId(id));
            coleccion.replaceOne(filtro, nuevoDocumento);

            System.out.println("Ojeto actualizado en mongo");
        } catch (Exception e) {
            System.out.println("Error al actualizar el documento en MongoDB");
        }
    }
}