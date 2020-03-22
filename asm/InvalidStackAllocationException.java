package asm;

public class InvalidStackAllocationException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidStackAllocationException() {
    super("Invalid stack allocation");
  }
}
