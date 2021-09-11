package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;
import com.lielamar.connections.exceptions.ConnectionNotOpenException;
import com.lielamar.connections.exceptions.EntryNotFoundException;
import com.lielamar.connections.serializable.SerializableDocument;
import com.lielamar.connections.serializable.SerializableObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MongoDBConnection extends DatabaseConnection {

    public MongoDBConnection(MongoDBConnectionConfig config) { super(config); }


    @Override
    public void connect() {
        String uri;

        if(this.getConfig().isSRVProtocol())
            uri = String.format("mongodb+srv://%s:%s@%s/?w=majority", this.getConfig().getUsername(), this.getConfig().getPassword(), this.getConfig().getHost());
        else
            uri = String.format("mongodb://%s:%s@%s:%s/", this.getConfig().getUsername(), this.getConfig().getPassword(), this.getConfig().getHost(), this.getConfig().getPort());

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        this.connection = MongoClients.create(settings);
    }

    @Override
    public boolean isClosed() {
        if(getConnection() == null) return true;

        try {
            getConnection().getClusterDescription();
            return false;
        } catch(Exception exception) {
            return true;
        }
    }

    @Override
    public @Nullable MongoClient getConnection() { return (MongoClient) super.getConnection(); }

    @Override
    public @NotNull MongoDBConnectionConfig getConfig() { return (MongoDBConnectionConfig) super.getConfig(); }


    @Override
    public @NotNull <T extends SerializableObject> T getObjectByIdentifier(@NotNull Supplier<T> supplier, @NotNull String identifier) throws ConnectionNotOpenException {
        if(this.getConnection() == null || this.isClosed())
            throw new ConnectionNotOpenException("MongoDB");

        T object = supplier.get();
        object.setIdentifier(identifier);

        MongoCollection<Document> collection = getConnection().getDatabase(object.getDatabase().getName()).getCollection(object.getCollection().getName());

        Document findQuery = new Document(this.getConfig().getIdentifierFieldName(), identifier);
        Document oldDocument = collection.find(findQuery).first();

        if(oldDocument != null && oldDocument.containsKey(object.getSystem().getName())) {
            Object systemDocument = oldDocument.get(object.getSystem().getName());

            if(systemDocument != null)
                object.read(new SerializableDocument((Document) systemDocument));
        }

        return object;
    }

    @Override
    public <T extends SerializableObject> void saveObjectByIdentifier(@NotNull T object) throws ConnectionNotOpenException {
        if(this.getConnection() == null || this.isClosed())
            throw new ConnectionNotOpenException("MongoDB");

        MongoCollection<Document> collection = this.getConnection().getDatabase(object.getDatabase().getName()).getCollection(object.getCollection().getName());

        Document oldDocument = collection.find(Filters.eq(this.getConfig().getIdentifierFieldName(), object.getIdentifier())).first();

        Document newDocument = new Document();
        newDocument.put(this.getConfig().getIdentifierFieldName(), object.getIdentifier());
        newDocument.put(object.getSystem().getName(), object.write());

        if(oldDocument == null) {
            collection.insertOne(newDocument);
        } else {
            Document updateQuery = new Document("$set", newDocument);
            collection.updateOne(Filters.eq(this.getConfig().getIdentifierFieldName(), object.getIdentifier()), updateQuery);
        }
    }

    @Override
    public <T extends SerializableObject> void deleteObjectByIdentifier(@NotNull Supplier<T> supplier, @NotNull String identifier) throws EntryNotFoundException, ConnectionNotOpenException {
        if(this.getConnection() == null || this.isClosed())
            throw new ConnectionNotOpenException("MongoDB");

        T object = supplier.get();
        object.setIdentifier(identifier);

        MongoCollection<Document> collection = this.getConnection().getDatabase(object.getDatabase().getName()).getCollection(object.getCollection().getName());

        Document findQuery = new Document(this.getConfig().getIdentifierFieldName(), identifier);
        Document oldDocument = collection.find(findQuery).first();

        if(oldDocument == null)
            throw new EntryNotFoundException(this.getConfig().getIdentifierFieldName(), identifier);

        if(oldDocument.containsKey(object.getSystem().getName())) {
            oldDocument.remove(object.getSystem().getName());

            Document updateQuery = new Document("$set", oldDocument);
            collection.updateOne(Filters.eq(this.getConfig().getIdentifierFieldName(), object.getIdentifier()), updateQuery);
        }
    }
}
