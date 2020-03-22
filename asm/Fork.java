package asm;

import static asm.Opcode.FORK;

public class Fork extends Instruction {
    private int address;

    public Fork(int address) {
        this.address = address;
    }

    @Override
    public void accept(AsmVisitor visitor) {
        visitor.visit(this);
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return FORK.toString();
    }
}
