package asm;

import static asm.Opcode.LOCK;

public class Lock extends Instruction {
    @Override
    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return LOCK.toString();
    }
}
