package com.lielamar.connections.serializable;

import com.lielamar.connections.connection.CollectionType;
import com.lielamar.connections.connection.DatabaseType;
import com.lielamar.connections.connection.SystemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SerializableObject implements Serializable {

    private String identifier;

    public SerializableObject() { this(null); }

    public SerializableObject(@Nullable String identifier) {
        this.identifier = identifier;
        init();
    }

    /**
     * Unique Identifier of the object in its collection
     *
     * @return id of the object
     */
    public @Nullable String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(@Nullable String identifier) {
        this.identifier = identifier;
    }

    protected abstract void init();

    /**
     * Database, Collection and System to use when saving/retrieving the object
     */
    public abstract @NotNull DatabaseType getDatabase();
    public abstract @NotNull CollectionType getCollection();
    public abstract @NotNull SystemType getSystem();
}