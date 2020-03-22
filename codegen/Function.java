package codegen;

public class Function {
  private String name;

  public String getName() {
    return name;
  }
  
  private Type type;
  
  public Type getType() {
    return type;
  }

  private Parameter[] parameters;

  public Parameter[] getParameters() {
    return parameters;
  }

  private Declaration[] declarations;

  public Declaration[] getDeclarations() {
    return declarations;
  }

  private Statement[] statements;

  public Statement[] getStatements() {
    return statements;
  }

  public Function(String name, String[] parameters, Declaration[] declarations,
      Statement[] statements) {
    this.type = Type.Int;
    this.name = name;
    this.parameters = new Parameter[parameters.length];
    for (int i = 0; i < parameters.length; i++)
      this.parameters[i] = new Parameter(Type.Int, parameters[i]);
    this.declarations = declarations;
    this.statements = statements;
  }
  
  public Function(Type type, String name, Parameter[] parameters, Declaration[] declarations,
      Statement[] statements) {
    this.type = type;
    this.name = name;
    this.parameters = parameters;
    this.declarations = declarations;
    this.statements = statements;
  }

  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
