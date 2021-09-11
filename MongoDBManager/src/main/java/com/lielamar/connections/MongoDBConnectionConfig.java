package com.lielamar.connections;

import com.lielamar.connections.config.DatabaseConnectionConfig;
import org.jetbrains.annotations.NotNull;

public interface MongoDBConnectionConfig extends DatabaseConnectionConfig {

    boolean isSRVProtocol();

}
