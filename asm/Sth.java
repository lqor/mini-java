package asm;
import static asm.Opcode.*;

public class Sth extends Instruction {
  @Override
  public String toString() {
    return STH.toString();
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
