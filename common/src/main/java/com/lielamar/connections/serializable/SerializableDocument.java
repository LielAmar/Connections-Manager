package com.lielamar.connections.serializable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SerializableDocument extends HashMap<String, Object> {

    public SerializableDocument() { super(); }
    public SerializableDocument(@NotNull Map<String, Object> document) { super(document); }
    public SerializableDocument(@NotNull String json) { super(new Gson().fromJson(json, new TypeToken<Map<String, Object>>(){}.getType())); }

    public @NotNull UUID getOrDefault(@NotNull String key, @NotNull UUID defaultValue) {
        return UUID.fromString(this.getOrDefault(key, defaultValue.toString()));
    }

    @SuppressWarnings("unchecked")
    public @NotNull SerializableDocument getOrDefault(@NotNull String key, @NotNull Map<String, Object> defaultValue) {
        return new SerializableDocument((Map<String, Object>) super.getOrDefault(key, defaultValue));
    }
    public @NotNull SerializableDocument getOrDefault(@NotNull String key, @NotNull SerializableDocument defaultValue) {
        return (SerializableDocument) super.getOrDefault(key, defaultValue);
    }

    public @Nullable String getString(@NotNull String key) { return (String) this.get(key); }
    public @Nullable Character getChar(@NotNull String key) { return (Character) this.get(key); }
    public @Nullable Boolean getBoolean(@NotNull String key) { return (Boolean) this.get(key); }
    public @Nullable Byte getByte(@NotNull String key) { return (Byte) this.get(key); }
    public @Nullable Short getShort(@NotNull String key) { return (Short) this.get(key); }
    public @Nullable Integer getInt(@NotNull String key) { return (Integer) this.get(key); }
    public @Nullable Long getLong(@NotNull String key) { return (Long) this.get(key); }
    public @Nullable Double getDouble(@NotNull String key) { return (Double) this.get(key); }
    public @Nullable Float getFloat(@NotNull String key) { return (Float) this.get(key); }
    public @Nullable String[] getStringArray(@NotNull String key) { return (String[]) this.get(key); }
    public @Nullable List<String> getStringList(@NotNull String key) { return (List<String>) this.get(key); }
    public @Nullable <T> List<T> getList(@NotNull String key, Supplier<T> instance) { return (List<T>) this.get(key); }
    public @Nullable SerializableDocument getDocument(@NotNull String key) { return (SerializableDocument) this.get(key); }

    public @NotNull String getOrDefault(@NotNull String key, @NotNull String defaultValue) { return (String) super.getOrDefault(key, defaultValue); }
    public @NotNull Character getOrDefault(@NotNull String key, char defaultValue) { return (Character) super.getOrDefault(key, defaultValue); }
    public @NotNull Boolean getOrDefault(@NotNull String key, boolean defaultValue) { return (Boolean) super.getOrDefault(key, defaultValue); }
    public @NotNull Byte getOrDefault(@NotNull String key, byte defaultValue) { return Byte.parseByte(super.getOrDefault(key, defaultValue) + ""); }
    public @NotNull Short getOrDefault(@NotNull String key, short defaultValue) { return Short.parseShort(super.getOrDefault(key, defaultValue) + ""); }
    public @NotNull Integer getOrDefault(@NotNull String key, int defaultValue) { return Integer.parseInt(super.getOrDefault(key, defaultValue) + ""); }
    public @NotNull Long getOrDefault(@NotNull String key, long defaultValue) { return Long.parseLong(super.getOrDefault(key, defaultValue) + ""); }
    public @NotNull Double getOrDefault(@NotNull String key, double defaultValue) { return (double) super.getOrDefault(key, defaultValue); }
    public @NotNull Float getOrDefault(@NotNull String key, float defaultValue) { return ((Double) super.getOrDefault(key, defaultValue)).floatValue(); }
    public @NotNull String[] getOrDefault(@NotNull String key, @NotNull String[] defaultValue) { return (String[]) this.getOrDefault(key, defaultValue); }
    public @NotNull List<String> getOrDefault(@NotNull String key, @NotNull List<String> defaultValue) { return this.getOrDefault(key, defaultValue); }
    public @NotNull <T> List<T> getOrDefault(@NotNull String key, @NotNull List<T> defaultValue, Supplier<T> instance) { return (List<T>) this.getOrDefault(key, defaultValue); }


    public static @NotNull SerializableDocument parse(@NotNull String json) {
        return new SerializableDocument(json);
    }
}