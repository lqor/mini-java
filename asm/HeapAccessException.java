package asm;

public class HeapAccessException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public HeapAccessException() {
    super("Invalid heap access");
  }
}
