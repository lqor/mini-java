package asm;
import static asm.Opcode.*;

public class Mod extends Instruction {
  @Override
  public String toString() {
    return MOD.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
