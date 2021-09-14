package com.lielamar.connections.primitives;

import com.lielamar.connections.serializable.Serializable;
import com.lielamar.connections.serializable.SerializableDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SerializableArrayList<T> extends ArrayList<T> implements Serializable {

    public SerializableArrayList(int initialCapacity) {
        super(initialCapacity);
    }


    @Override
    public @NotNull SerializableDocument write() {
        SerializableDocument document = new SerializableDocument();

        for(int i = 0; i < this.size(); i++)
            document.put(i + "", this.get(i).toString());

        return document;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(@Nullable SerializableDocument document) {
        if(document == null) return;

        for(int i = 0; i < document.keySet().size(); i++)
            this.add(null);

        for(String key : document.keySet())
            this.set(Integer.parseInt(key), (T) document.get(key));
    }
}