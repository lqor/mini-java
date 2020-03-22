package asm;
import static asm.Opcode.*;

public class Brc extends Instruction {
  private int target;

  public int getTarget() {
    return target;
  }
  
  public Brc(int target) {
    this.target = target;
  }

  @Override
  public String toString() {
    return BRC.toString() + " " + target;
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }
}
