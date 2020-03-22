package asm;
import static asm.Opcode.*;

public class Or extends Instruction {
  @Override
  public String toString() {
    return OR.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
