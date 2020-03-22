package codegen;

public class While extends Statement {
  private Condition cond;
  
  public Condition getCond() {
    return cond;
  }
  
  private Statement body;
  
  public Statement getBody() {
    return body;
  }
  
  private boolean doWhile;
  
  public boolean isDoWhile() {
    return doWhile;
  }
  
  public While(Condition cond, Statement body, boolean doWhile) {
    super();
    this.cond = cond;
    this.body = body;
    this.doWhile = doWhile;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
