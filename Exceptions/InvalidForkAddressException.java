package Exceptions;

import asm.InterpreterException;

public class InvalidForkAddressException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidForkAddressException() {
        super("Invalid fork address on stack");
    }
}
