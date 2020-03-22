package asm;
import static asm.Opcode.*;

public class Ldi extends Instruction {
  private int value;
  
  public int getValue() {
    return value;
  }
  
  public Ldi(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return LDI.toString() + " " + value;
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
