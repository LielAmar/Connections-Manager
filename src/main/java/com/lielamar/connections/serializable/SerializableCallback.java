package com.lielamar.connections.serializable;

public interface SerializableCallback<T extends SerializableObject> {

    void callback(T element);

}
