package com.lielamar.connections.primitives;

import com.lielamar.connections.serializable.SerializableDocument;
import com.lielamar.connections.serializable.Serializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SerializableArrayListOfSerializables<T extends Serializable> extends ArrayList<T> implements Serializable {

    private final Supplier<T> supplier;

    public SerializableArrayListOfSerializables(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public @NotNull SerializableDocument write() {
        SerializableDocument document = new SerializableDocument();

        for(int i = 0; i < this.size(); i++)
            document.put(i + "", this.get(i).write());

        return document;
    }

    @Override
    public void read(@Nullable SerializableDocument document) {
        if(document == null) return;

        for(int i = 0; i < document.keySet().size(); i++)
            this.add(null);

        for(String key : document.keySet()) {
            T element = supplier.get();
            SerializableDocument elementDocument = document.getDocument(key);

            if(elementDocument != null) {
                element.read(new SerializableDocument());
                this.set(Integer.parseInt(key), element);
            }
        }
    }
}