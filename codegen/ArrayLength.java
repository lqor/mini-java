package codegen;

public class ArrayLength extends Expression {
  private Expression array;
  
  public Expression getArray() {
    return array;
  }
  
  public ArrayLength(Expression array) {
    this.array = array;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
