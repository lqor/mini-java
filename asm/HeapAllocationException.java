package asm;

public class HeapAllocationException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public HeapAllocationException() {
    super("Invalid heap allocation");
  }
}
