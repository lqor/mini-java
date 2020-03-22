package asm;

public class InvalidFramePointerException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidFramePointerException() {
    super("Invalid frame pointer on stack; the stack has been destroyed by the program");
  }
}
