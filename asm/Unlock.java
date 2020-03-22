package asm;

import static asm.Opcode.UNLOCK;

public class Unlock extends Instruction {
    @Override
    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public String toString() {
        return UNLOCK.toString();
    }
}
