package Exceptions;

import asm.InterpreterException;

public class SomeThreadNotClosed extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public SomeThreadNotClosed() {
        super("Einige Threads wurden am Ende des Programms nicht geschlossen!");
    }
}
