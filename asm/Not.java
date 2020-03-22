package asm;
import static asm.Opcode.*;

public class Not extends Instruction {
  @Override
  public String toString() {
    return NOT.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
