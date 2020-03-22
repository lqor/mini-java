package codegen;

public class Break extends Statement {

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
