package codegen;

public class False extends Condition {

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
