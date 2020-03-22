package Exceptions;

import asm.InterpreterException;

public class InvalidNumberOfForkParametersException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidNumberOfForkParametersException() {
        super("Invalid number of fork parameters");
    }
}
