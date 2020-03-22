package asm;

public abstract class InterpreterException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InterpreterException(String message) {
    super(message);
  }
}
