package com.lielamar.connections.primitives;

import com.lielamar.connections.serializable.SerializableDocument;
import com.lielamar.connections.serializable.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class SerializableUUIDList extends ArrayList<UUID> implements Serializable {

    @Override
    public @NotNull SerializableDocument write() {
        SerializableDocument document = new SerializableDocument();

        for(int i = 0; i < this.size(); i++)
            document.put(i + "", this.get(i).toString());

        return document;
    }

    @Override
    public void read(@Nullable SerializableDocument document) {
        if(document == null) return;

        for(int i = 0; i < document.keySet().size(); i++)
            this.add(null);

        String keyUUID;
        for(String key : document.keySet()) {
            keyUUID = document.getString(key);

            if(keyUUID != null)
                this.set(Integer.parseInt(key), UUID.fromString(keyUUID));
        }
    }
}