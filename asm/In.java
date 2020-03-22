package asm;
import static asm.Opcode.*;

public class In extends Instruction {

  @Override
  public String toString() {
    return IN.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
