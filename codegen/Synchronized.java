package codegen;

import codegen.Expression;
import codegen.ProgramVisitor;
import codegen.Statement;

public class Synchronized extends Statement {
  private Expression mutex;
  private Statement[] criticalSection;

  public Expression getMutex() {
    return mutex;
  }

  public Statement[] getCriticalSection() {
    return criticalSection;
  }

  public Synchronized(Expression mutex, Statement[] criticalSection) {
    super();
    this.mutex = mutex;
    this.criticalSection = criticalSection;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
