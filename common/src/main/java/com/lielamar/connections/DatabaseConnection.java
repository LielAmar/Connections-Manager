package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;
import com.lielamar.connections.exceptions.ConnectionNotOpenException;
import com.lielamar.connections.exceptions.EntryNotFoundException;
import com.lielamar.connections.exceptions.SystemNotFoundException;
import com.lielamar.connections.serializable.SerializableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Retrieves an object's data from the database
     *
     * @param supplier                      Supplier of the object type
     * @param identifier                    Identifier of the entry to retrieve
     * @return                              A CompletableFuture of the loaded object
     * @throws ConnectionNotOpenException   If the connection is not open
     */
    public abstract @NotNull <T extends SerializableObject> CompletableFuture<@Nullable T> getObjectByIdentifier(@NotNull Supplier<@NotNull T> supplier, @NotNull String identifier) throws ConnectionNotOpenException;

    /**
     * Retrieves objects' data from the database
     *
     * @param supplier                      Supplier of the object type
     * @return                              A CompletableFuture of the loaded objects
     * @throws ConnectionNotOpenException   If the connection is not open
     */
    public abstract <T extends SerializableObject> @NotNull CompletableFuture<@NotNull List<@Nullable T>> getAllObjects(@NotNull Supplier<@NotNull T> supplier) throws ConnectionNotOpenException;

    /**
     * Saves an object's data in the database
     *
     * @param object                        Object to save in the database
     * @throws ConnectionNotOpenException   If the connection is not open
     */
    public abstract <T extends SerializableObject> void saveObjectByIdentifier(@NotNull T object) throws ConnectionNotOpenException;

    /**
     * Saves an object's data in the database
     *
     * @param supplier                      Supplier of the object type
     * @throws EntryNotFoundException       If there's no entry with the given identifier in the database
     * @throws ConnectionNotOpenException   If the connection is not open
     * @throws SystemNotFoundException      If there's no index for the system within the object's data
     */
    public abstract <T extends SerializableObject> void deleteObjectByIdentifier(@NotNull Supplier<@NotNull T> supplier, @NotNull String identifier) throws EntryNotFoundException, ConnectionNotOpenException, SystemNotFoundException;
}
