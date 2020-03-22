package codegen;

public class Switch extends Statement {
  private SwitchCase[] cases;
  
  public SwitchCase[] getCases() {
    return cases;
  }
  
  private Statement _default;
  
  public Statement getDefault() {
    return _default;
  }
  
  private Expression switchExpression;
  
  public Expression getSwitchExpression() {
    return switchExpression;
  }

  public Switch(SwitchCase[] cases, Statement _default, Expression switchExpression) {
    this.cases = cases;
    this._default = _default;
    this.switchExpression = switchExpression;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
