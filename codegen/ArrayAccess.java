package codegen;

public class ArrayAccess extends Expression {
  private Expression array;
  
  public Expression getArray() {
    return array;
  }
  
  private Expression index;
  
  public Expression getIndex() {
    return index;
  }
  
  public ArrayAccess(Expression array, Expression index) {
    this.array = array;
    this.index = index;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
