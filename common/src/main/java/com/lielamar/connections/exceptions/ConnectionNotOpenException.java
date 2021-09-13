package com.lielamar.connections.exceptions;

public class ConnectionNotOpenException extends Exception {

    private final String connectionType;

    public ConnectionNotOpenException(String connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String getMessage() { return "Your " + connectionType + " connection is not open!"; }
}
