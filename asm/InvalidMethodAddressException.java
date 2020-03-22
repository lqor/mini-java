package asm;

public class InvalidMethodAddressException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidMethodAddressException() {
    super("Invalid method address");
  }
}
