package codegen;

public class InvalidMainException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public InvalidMainException() {
    super("Main function is missing");
  }

}
