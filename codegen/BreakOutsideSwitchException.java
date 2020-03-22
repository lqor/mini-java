package codegen;

public class BreakOutsideSwitchException extends CodeGenException {
  private static final long serialVersionUID = 1L;

  public BreakOutsideSwitchException() {
    super("Break outside of switch statement");
  }

}
