package codegen;

public class ExpressionStatement extends Statement {
  private Expression expr;
  
  public Expression getExpression() {
    return expr;
  }
  
  public ExpressionStatement(Expression expr) {
    this.expr = expr;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
