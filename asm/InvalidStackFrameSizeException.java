package asm;

public class InvalidStackFrameSizeException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidStackFrameSizeException() {
    super("Invalid stack frame size");
  }
}
