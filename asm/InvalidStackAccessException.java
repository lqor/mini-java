package asm;

public class InvalidStackAccessException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidStackAccessException() {
    super("Invalid stack accesss");
  }
}
