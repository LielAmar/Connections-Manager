package com.lielamar.connections.exceptions;

public class EntryNotFoundException extends Exception {

    private final String identifierName;
    private final String identifier;

    public EntryNotFoundException(String identifierName, String identifier) {
        this.identifierName = identifierName;
        this.identifier = identifier;
    }

    @Override
    public String getMessage() { return "No record found for the provided entry (" + identifierName + " as " + identifier + ")!"; }
}
