package codegen;

public class Join extends Expression {
  private Expression threadId;
  
  public Expression getThreadId() {
    return threadId;
  }
  
  public Join(Expression threadId) {
    super();
    this.threadId = threadId;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
