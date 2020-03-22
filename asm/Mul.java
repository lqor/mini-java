package asm;
import static asm.Opcode.*;

public class Mul extends Instruction {
  @Override
  public String toString() {
    return MUL.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
