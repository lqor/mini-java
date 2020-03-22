package codegen;

public class EmptyStatement extends Statement {

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
