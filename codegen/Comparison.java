package codegen;

public class Comparison extends Condition {
  private Expression lhs;
  
  public Expression getLhs() {
    return lhs;
  }
  
  private Comp operator;
  
  public Comp getOperator() {
    return operator;
  }
  
  private Expression rhs;
  
  public Expression getRhs() {
    return rhs;
  }
  
  public Comparison(Expression lhs, Comp operator, Expression rhs) {
    super();
    this.lhs = lhs;
    this.operator = operator;
    this.rhs = rhs;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
