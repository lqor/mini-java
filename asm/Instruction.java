package asm;

public abstract class Instruction {
  public abstract void accept(AsmVisitor visitor);
}
