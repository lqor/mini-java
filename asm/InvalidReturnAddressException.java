package asm;

public class InvalidReturnAddressException extends InterpreterException {
  private static final long serialVersionUID = 1L;

  public InvalidReturnAddressException() {
    super("Invalid return address on stack; the stack has been destroyed by the program");
  }
}
