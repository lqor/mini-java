package Exceptions;

import asm.InterpreterException;

public class NoSuchHeapAddressException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public NoSuchHeapAddressException() {
        super("Diese Adresse wurde noch nie allokiert!");
    }
}
