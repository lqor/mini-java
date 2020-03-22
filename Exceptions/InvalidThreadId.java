package Exceptions;

import asm.InterpreterException;

public class InvalidThreadId  extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidThreadId() {
        super("Keine gültige ThreadID!");
    }
}
