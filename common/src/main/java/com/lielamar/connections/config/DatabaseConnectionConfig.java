package com.lielamar.connections.config;

import org.jetbrains.annotations.NotNull;

public interface DatabaseConnectionConfig extends ConnectionConfig {

    @NotNull String getIdentifierFieldName();
}
