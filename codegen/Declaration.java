package codegen;

public class Declaration {
  private Type type;
  
  public Type getType() {
    return type;
  }
  
  private String[] names;
  
  public String[] getNames() {
    return names;
  }
  
  public Declaration(String[] names) {
    this.type = Type.Int;
    this.names = names;
  }
  
  public Declaration(String a) {
    this.type = Type.Int;
    this.names = new String [] { a };
  }
  
  public Declaration(Type type, String[] names) {
    this.type = type;
    this.names = names;
  }
  
  public Declaration(Type type, String a) {
    this.type = type;
    this.names = new String [] { a };
  }

  public Declaration(String a, String b) {
    this.type = Type.Int;
    this.names = new String [] { a, b };
  }
  
  public Declaration(Type type, String a, String b) {
    this.type = type;
    this.names = new String [] { a, b };
  }

  public void accept(ProgramVisitor visitor) {
    visitor.visit(this);
  }
}
