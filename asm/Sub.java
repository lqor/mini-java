package asm;
import static asm.Opcode.*;

public class Sub extends Instruction {

  @Override
  public String toString() {
    return SUB.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
