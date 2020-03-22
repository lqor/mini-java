package asm;
import static asm.Opcode.*;

public class Div extends Instruction {
  @Override
  public String toString() {
    return DIV.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
