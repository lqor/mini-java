package codegen;

public class UnknownVariableException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public UnknownVariableException(String name) {
    super("Unknown variable '" + name + "'");
  }

}
