package codegen;

public class Call extends Expression {
  private String functionName;
  private boolean fork;
  
  public String getFunctionName() {
    return functionName;
  }
  
  private Expression[] arguments;
  
  public Expression[] getArguments() {
    return arguments;
  }

  public boolean isFork() {
    return fork;
  }

  public Call(String functionName, Expression[] arguments, boolean fork) {
    super();
    this.functionName = functionName;
    this.arguments = arguments;
    this.fork = fork;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }

}
