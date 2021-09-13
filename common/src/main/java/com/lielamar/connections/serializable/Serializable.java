package com.lielamar.connections.serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Serializable extends java.io.Serializable {

    /**
     * Creates a document that will be used when inserting this data to the database
     *
     * @return   Created document to insert to the database
     */
    @NotNull
    SerializableDocument write();

    /**
     * Updates internal values of the Serializable according to the given document
     *
     * @param document   Document to read
     */
    void read(@Nullable SerializableDocument document);
}