package Exceptions;

import asm.InterpreterException;

public class InvalidJoinTarget extends InterpreterException {
    private static final long serialVersionUID = 1L;

    public InvalidJoinTarget() {
        super("You cannot join the main method!");
    }
}
