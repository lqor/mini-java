package asm;

public class InvalidNumberOfMethodParametersException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidNumberOfMethodParametersException() {
    super("Invalid number of method parameters");
  }
}
