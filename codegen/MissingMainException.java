package codegen;

public class MissingMainException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public MissingMainException() {
    super("Main function must not have parameters");
  }

}
