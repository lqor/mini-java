package codegen;

public class ArrayIndexAssignment extends Statement {
  private Expression array;
  
  public Expression getArray() {
    return array;
  }
  
  private Expression index;
  
  public Expression getIndex() {
    return index;
  }
  
  private Expression expression;
  
  public Expression getExpression() {
    return expression;
  }
  
  public ArrayIndexAssignment(Expression array, Expression index, Expression expression) {
    this.array = array;
    this.index = index;
    this.expression = expression;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
