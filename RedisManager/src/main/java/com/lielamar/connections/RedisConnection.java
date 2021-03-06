package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;
import com.lielamar.connections.exceptions.ConnectionNotOpenException;
import com.lielamar.connections.exceptions.EntryNotFoundException;
import com.lielamar.connections.serializable.SerializableDocument;
import com.lielamar.connections.serializable.SerializableObject;
import com.lielamar.connections.types.TypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RedisConnection extends DatabaseConnection {

    public RedisConnection(DatabaseConnectionConfig config) { super(config); }


    @Override
    public void connect() {
        JedisShardInfo sharedInfo = new JedisShardInfo(this.getConfig().getHost(), this.getConfig().getPort());
        sharedInfo.setUser(this.getConfig().getUsername());
        sharedInfo.setPassword(this.getConfig().getPassword());

        this.connection = new Jedis(sharedInfo);
        ((Jedis)this.connection).connect();
    }

    @Override
    public boolean isClosed() {
        if(getConnection() == null) return true;

        return !getConnection().isConnected() || getConnection().isBroken();
    }

    @Override
    public @Nullable Jedis getConnection() { return (Jedis) super.getConnection(); }


    @Override
    public @NotNull <T extends SerializableObject> CompletableFuture<@Nullable T> getObjectByIdentifier(@NotNull Supplier<@NotNull T> supplier, @NotNull String identifier) throws ConnectionNotOpenException {
        if(this.getConnection() == null || this.isClosed())
            throw new ConnectionNotOpenException("Redis");

        return CompletableFuture.supplyAsync(() -> {
            T object = supplier.get();
            object.setIdentifier(identifier);

            String baseHash = object.getCollection().getName() + ":" + object.getSystem().getName();

            SerializableDocument loadedData = this.load(baseHash, identifier);
            if(!loadedData.isEmpty()) {
                object.read(loadedData);
                return object;
            }

            return null;
        });
    }

    private @NotNull SerializableDocument load(@NotNull String baseHash, @NotNull String identifier) {
        assert getConnection() != null;

        SerializableDocument data = new SerializableDocument();

        String[] basePath = baseHash.split(":");

        this.getConnection().keys(baseHash + "*").forEach(key -> {
            String[] path = key.split(":");

            if(basePath.length + 1 == path.length) {
                if(this.getConnection().hget(key, identifier) != null)
                    data.put(path[path.length - 1], TypeUtils.castToRightType(this.getConnection().hget(key, identifier)));
            } else if(basePath.length + 1 < path.length && !data.containsKey(path[basePath.length])) {
                data.put(path[basePath.length], this.load(baseHash + ":" + path[basePath.length], identifier));
            }
        });

        return data;
    }


    @Override
    public @NotNull <T extends SerializableObject> CompletableFuture<@NotNull List<@Nullable T>> getAllObjects(@NotNull Supplier<@NotNull T> supplier) throws ConnectionNotOpenException {
        if(this.getConnection() == null || this.isClosed())
            throw new ConnectionNotOpenException("Redis");

        return CompletableFuture.supplyAsync(() -> {
            List<T> elements = new ArrayList<>();

            T instance = supplier.get();

            Set<String> identifiers = new HashSet<>();

            for(String key : this.getConnection().keys(instance.getCollection().getName() + ":")) {
                identifiers.addAll(this.getConnection().hkeys(key)
                        .stream().map(String::toString)
                        .collect(Collectors.toSet()));
            }

            for(String identifier : identifiers) {
                T object = supplier.get();
                object.setIdentifier(identifier);
                String baseHash = instance.getCollection().getName() + ":" + object.getSystem().getName();
                object.read(new SerializableDocument(load(baseHash, identifier)));
            }

            return elements;
        });
    }


    @Override
    public <T extends SerializableObject> void saveObjectByIdentifier(@NotNull T object) throws ConnectionNotOpenException {
        if(getConnection() == null || isClosed())
            throw new ConnectionNotOpenException("Redis");

        String baseHash = object.getCollection().getName() + ":" + object.getSystem().getName();
        SerializableDocument data = object.write();

        if(object.getIdentifier() != null)
            this.save(data, baseHash, object.getIdentifier());
    }

    private void save(@NotNull SerializableDocument data, @NotNull String baseHash, @NotNull String identifier) {
        assert getConnection() != null;

        for(String key : data.keySet()) {
            String objectHash = baseHash + ":" + key;

            if(data.get(key) instanceof SerializableDocument)
                save((SerializableDocument) data.get(key), objectHash, identifier);
            else
                this.getConnection().hset(objectHash, identifier, data.get(key).toString());
        }
    }


    @Override
    public <T extends SerializableObject> void deleteObjectByIdentifier(@NotNull Supplier<@NotNull T> supplier, @NotNull String identifier) throws EntryNotFoundException, ConnectionNotOpenException {
        if(getConnection() == null || isClosed())
            throw new ConnectionNotOpenException("Redis");

        T object = supplier.get();
        object.setIdentifier(identifier);

        String baseHash = object.getCollection().getName() + ":" + object.getSystem().getName();
        this.getConnection().keys(baseHash + "*").forEach(key -> this.getConnection().hdel(key, identifier));
    }
}
