package asm;
import static asm.Opcode.*;

public class Pop extends Instruction {
  private int register;
  
  public int getRegister() {
    return register;
  }
  
  public Pop(int register) {
    this.register = register;
  }

  @Override
  public String toString() {
    return POP.toString() + " " + register;
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }
}
