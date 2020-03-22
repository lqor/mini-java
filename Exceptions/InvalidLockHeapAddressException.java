package Exceptions;

import asm.InterpreterException;

public class InvalidLockHeapAddressException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidLockHeapAddressException() {
        super("Es liegt eine invalide heapAdresse auf dem Stack vor!");
    }
}
