package asm;
import static asm.Opcode.*;

public class Halt extends Instruction {

  @Override
  public String toString() {
    return HALT.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
