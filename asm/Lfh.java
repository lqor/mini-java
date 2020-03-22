package asm;
import static asm.Opcode.*;

public class Lfh extends Instruction {
  @Override
  public String toString() {
    return LFH.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
