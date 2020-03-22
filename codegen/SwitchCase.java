package codegen;

public class SwitchCase {
  private Number number;
  
  public Number getNumber() {
    return number;
  }
  
  private Statement caseStatement;
  
  public Statement getCaseStatement() {
    return caseStatement;
  }
  
  public SwitchCase(Number number, Statement caseStatement) {
    this.number = number;
    this.caseStatement = caseStatement;
  }
}
