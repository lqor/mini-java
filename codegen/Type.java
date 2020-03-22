package codegen;

public enum Type {
  Int, IntArray;
  
  @Override
  public String toString() {
    switch(this) {
      case Int:
        return "int";
      case IntArray:
        return "int[]";
    }
    throw new RuntimeException("Unreachable");
  }
}
