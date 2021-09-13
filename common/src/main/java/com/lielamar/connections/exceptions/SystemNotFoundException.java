package com.lielamar.connections.exceptions;

public class SystemNotFoundException extends Exception {

    private final String systemType;

    public SystemNotFoundException(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public String getMessage() { return "The system " + systemType + " was not found in the object's document!"; }
}
