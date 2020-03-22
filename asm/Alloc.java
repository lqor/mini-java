package asm;
import static asm.Opcode.*;

public class Alloc extends Instruction {
  @Override
  public String toString() {
    return ALLOC.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
