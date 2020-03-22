package asm;
import static asm.Opcode.*;

public class Call extends Instruction {
  private int argCount;
  
  public int getArgCount() {
    return argCount;
  }
  
  public Call(int argCount) {
    this.argCount = argCount;
  }

  @Override
  public String toString() {
    return CALL.toString() + " " + argCount;
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }
}
