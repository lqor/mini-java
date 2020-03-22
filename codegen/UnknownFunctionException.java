package codegen;

public class UnknownFunctionException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public UnknownFunctionException(String name) {
    super("Unknown function '" + name + "'");
  }

}
