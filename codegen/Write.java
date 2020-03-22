package codegen;

public class Write extends Expression {
  private Expression expression;
  
  public Expression getExpression() {
    return expression;
  }
  
  public Write(Expression expression) {
    super();
    this.expression = expression;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
