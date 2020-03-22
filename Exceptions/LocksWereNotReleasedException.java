package Exceptions;

import asm.InterpreterException;

public class LocksWereNotReleasedException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public LocksWereNotReleasedException() {
        super("Am Ende des Programms sind nocht nicht ausgelockte HeapBereiche vorhanden!");
    }
}
