package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;

public interface MongoDBConnectionConfig extends DatabaseConnectionConfig {

    boolean isSRVProtocol();

}
