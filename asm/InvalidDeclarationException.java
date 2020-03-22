package asm;

public class InvalidDeclarationException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidDeclarationException() {
    super("Invalid declaration (not at the beginning of a method)");
  }
}
