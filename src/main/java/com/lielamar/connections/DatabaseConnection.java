package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;
import com.lielamar.connections.exceptions.ConnectionNotOpenException;
import com.lielamar.connections.exceptions.EntryNotFoundException;
import com.lielamar.connections.serializable.SerializableObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class DatabaseConnection extends Connection {

    public DatabaseConnection(DatabaseConnectionConfig config) {
        super(config);
    }


    @Override
    public @NotNull DatabaseConnectionConfig getConfig() { return (DatabaseConnectionConfig) super.getConfig(); }


    /**
     * Retrieves an entry data from the connection
     *
     * @param identifier                    Identifier of the entry to retrieve
     * @return                              Retrieved data
     * @throws ConnectionNotOpenException   If the connection is not open
     */
    public abstract @NotNull <T extends SerializableObject> CompletableFuture<T> getObjectByIdentifier(@NotNull Supplier<T> supplier, @NotNull String identifier) throws ConnectionNotOpenException;

    public abstract <T extends SerializableObject> @NotNull CompletableFuture<List<T>> getAllObjects(@NotNull Supplier<T> supplier) throws ConnectionNotOpenException;

    public abstract <T extends SerializableObject> void saveObjectByIdentifier(@NotNull T object) throws ConnectionNotOpenException;

    public abstract <T extends SerializableObject> void deleteObjectByIdentifier(@NotNull Supplier<T> supplier, @NotNull String identifier) throws EntryNotFoundException, ConnectionNotOpenException;


//    /**
//     * Overwrites an existing entry's data in the connection
//     *
//     * @param identifier   Identifier of the entry to overwrite
//     * @param newData      New data to set
//     * @throws             EntryNotFoundException - If no entry with the given identifier was found
//     */
//    public abstract void setDataByIdentifier(String identifier, Map<String, Object> newData) throws EntryNotFoundException;
//
//    /**
//     * Adds a new entry with its data to the connection
//     *
//     * @param identifier        Identifier of the new entry
//     * @param dataToInsert      Data to insert
//     * @throws                  EntryAlreadyExistsException - If an entry with the given identifier already exists
//     */
//    public abstract void insertDataByIdentifier(String identifier, Map<String, Object> dataToInsert) throws EntryAlreadyExistsException;
//
//    /**
//     * Deletes an existing entry's data in the connection
//     *
//     * @param identifier   Identifier of the entry to delete
//     * @throws             EntryNotFoundException - If no entry with the given identifier was found
//     */
//    public abstract void deleteDataByIdentifier(String identifier) throws EntryNotFoundException;
}
