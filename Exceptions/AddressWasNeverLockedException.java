package Exceptions;

import asm.InterpreterException;

public class AddressWasNeverLockedException  extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public AddressWasNeverLockedException() {
        super("Man versucht eine nie blockierte Adresse freizugeben!");
    }
}
