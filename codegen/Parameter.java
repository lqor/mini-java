package codegen;

public class Parameter {
  private Type type;
  
  public Type getType() {
    return type;
  }
  
  private String name;
  
  public String getName() {
    return name;
  }
  
  public Parameter(Type type, String name) {
    this.type = type;
    this.name = name;
  }
}
