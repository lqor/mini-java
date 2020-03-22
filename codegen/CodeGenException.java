package codegen;

public abstract class CodeGenException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CodeGenException(String message) {
    super(message);
  }
}
