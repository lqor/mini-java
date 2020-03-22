package Exceptions;

import asm.InterpreterException;

public class InvalidHaltPlaceException extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidHaltPlaceException() {
        super("Halt Instruction wurde in nicht Main-Methode ausgeführt!");
    }
}
