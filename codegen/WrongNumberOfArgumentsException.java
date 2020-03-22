package codegen;

public class WrongNumberOfArgumentsException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public WrongNumberOfArgumentsException() {
    super("Wrong number of function arguments");
  }

}
