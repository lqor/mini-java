package asm;
import static asm.Opcode.*;

public class Cmp extends Instruction {
  private CompareType compareType;
  
  public CompareType getCompareType() {
    return compareType;
  }
  
  public Cmp(CompareType compareType) {
    this.compareType = compareType;
  }
  
  @Override
  public String toString() {
    switch (compareType) {
      case EQ:
        return CMP.toString() + " EQUALS";
      case LT:
        return CMP.toString() + " LESS";
    }
    throw new RuntimeException("Unreachable");
  }

  @Override
  public void accept(AsmVisitor visitor) {
    visitor.visit(this);
  }

}
