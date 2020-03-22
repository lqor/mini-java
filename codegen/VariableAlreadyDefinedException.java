package codegen;

public class VariableAlreadyDefinedException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public VariableAlreadyDefinedException(String name) {
    super("Variable '" + name + "' is already defined");
  }

}
