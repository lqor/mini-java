package asm;
import static asm.Opcode.*;

public class Return extends Instruction {
  private int cells;
  
  public int getCells() {
    return cells;
  }
  
  public Return(int cells) {
    this.cells = cells;
  }

  @Override
  public String toString() {
    return RETURN.toString() + " " + cells;
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
