package asm;
import static asm.Opcode.*;

public class And extends Instruction {
  @Override
  public String toString() {
    return AND.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
