package codegen;

public class Read extends Expression {
  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
