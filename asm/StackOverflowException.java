package asm;

public class StackOverflowException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public StackOverflowException() {
    super("Stack overflow");
  }
}
