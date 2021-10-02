package com.lielamar.connections;

import com.lielamar.connections.config.ConnectionConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

public abstract class Connection {

    protected Closeable connection;

    protected ConnectionConfig config;

    public Connection(@Nullable ConnectionConfig config) {
        this.config = config;
    }


    public @Nullable ConnectionConfig getConfig() { return this.config; }
    public @Nullable Closeable getConnection() {
        return this.connection;
    }

    public void close() throws IOException {
        if(this.connection != null)
            this.connection.close();
    }
    public boolean isOpen() { return !isClosed(); }

    public abstract boolean isClosed();
    public abstract void connect();
}
