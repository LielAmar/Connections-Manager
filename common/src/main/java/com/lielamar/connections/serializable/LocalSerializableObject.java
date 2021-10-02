package com.lielamar.connections.serializable;

import com.lielamar.connections.connection.CollectionType;
import com.lielamar.connections.connection.DatabaseType;
import com.lielamar.connections.connection.SystemType;

public abstract class LocalSerializableObject extends SerializableObject {

    public LocalSerializableObject(String identifier) {
        super(identifier);
    }


    @Override
    public DatabaseType getDatabase() { return null; }

    @Override
    public CollectionType getCollection() { return null; }

    @Override
    public SystemType getSystem() { return null; }

}
