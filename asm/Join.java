package asm;

import static asm.Opcode.JOIN;

public class Join extends Instruction{
    @Override
    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return JOIN.toString();
    }
}
