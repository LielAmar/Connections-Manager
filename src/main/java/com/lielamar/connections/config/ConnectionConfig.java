package com.lielamar.connections.config;

import org.jetbrains.annotations.NotNull;

public interface ConnectionConfig {

    @NotNull String getHost();
    int getPort();

    @NotNull String getUsername();
    @NotNull String getPassword();
}
