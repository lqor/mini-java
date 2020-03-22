package Exceptions;

import codegen.CodeGenException;

public class NoStatementsInSynchronizedException extends CodeGenException {
    private static final long serialVersionUID = 1L;

    public NoStatementsInSynchronizedException() {
        super("Es ist kein Statement in Synchronized-Block vorhanden");
    }
}
