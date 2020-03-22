package Exceptions;

import asm.InterpreterException;

public class DeadlockException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public DeadlockException() {
        super("Kein Thread ist lauffähig, es liegt also Deadlock vor!");
    }
}
