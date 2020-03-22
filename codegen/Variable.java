package codegen;

public class Variable extends Expression {
  private String name;
  
  public String getName() {
    return name;
  }
  
  public Variable(String name) {
    super();
    this.name = name;
  }

  @Override
  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
