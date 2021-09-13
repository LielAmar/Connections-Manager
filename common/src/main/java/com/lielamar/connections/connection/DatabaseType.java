package com.lielamar.connections.connection;

public class DatabaseType {

    private final String databaseType;

    public DatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getName() { return databaseType; }
}
