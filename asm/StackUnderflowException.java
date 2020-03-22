package asm;

public class StackUnderflowException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public StackUnderflowException() {
    super("Stack underflow");
  }
}
