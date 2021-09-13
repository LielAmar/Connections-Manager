package com.lielamar.connections.exceptions;

public class EntryAlreadyExistsException extends Exception {

    private final String identifierName;
    private final String identifier;

    public EntryAlreadyExistsException(String identifierName, String identifier) {
        this.identifierName = identifierName;
        this.identifier = identifier;
    }

    @Override
    public String getMessage() { return "A record for the provided entry already exists (" + identifierName + " \"" + identifier + "\")!"; }
}
