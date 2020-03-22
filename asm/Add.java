package asm;
import static asm.Opcode.*;

public class Add extends Instruction {
  @Override
  public String toString() {
    return ADD.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
