package asm;

public class InvalidJumpTargetException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidJumpTargetException() {
    super("Invalid jump target");
  }
}
