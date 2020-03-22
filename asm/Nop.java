package asm;
import static asm.Opcode.*;

public class Nop extends Instruction {
  @Override
  public String toString() {
    return NOP.toString();
  }


  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
