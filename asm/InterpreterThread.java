package asm;

public class InterpreterThread {
    private int id;
    private int[] stack;
    private int stackPointer;
    private int programCounter;
    private int framePointer;
    private int[] registers;

    private int kCounter;
    public boolean fertig;
    private int returnValueByReturn;

    private boolean declAllowed = true;

    public InterpreterThread(int id, int[] stack, int stackPointer, int programCounter, int framePointer, int[] registers) {
        this.id = id;
        this.stack = stack;
        this.stackPointer = stackPointer;
        this.programCounter = programCounter;
        this.framePointer = framePointer;
        this.registers = registers;
    }

    public InterpreterThread(int id) {
        this.id = id;
        this.stack = new int[128];
        this.stackPointer = -1;
        this.programCounter = 0;
        this.framePointer = -1;
        this.registers = new int[2];
        this.fertig = false;
        this.kCounter = 0;
    }

    public int pop() {
        if (stackPointer < 0)
            throw new StackUnderflowException();
        return stack[stackPointer--];
    }

    public void push(int value) {
        stackPointer++;
        if (stackPointer >= stack.length)
            throw new StackOverflowException();
        stack[stackPointer] = value;
    }

    public boolean isDeclAllowed() {
        return declAllowed;
    }

    public void setDeclAllowed(boolean declAllowed) {
        this.declAllowed = declAllowed;
    }

    public int getReturnValueByReturn() {
        return returnValueByReturn;
    }

    public void setReturnValueByReturn(int returnValueByReturn) {
        this.returnValueByReturn = returnValueByReturn;
    }

    public void inckCounter() {
        this.kCounter++;
    }

    public int getkCounter() {
        return kCounter;
    }

    public void incProgramCounter() {
        this.programCounter++;
    }

    public void setkCounter(int kCounter) {
        this.kCounter = kCounter;
    }

    public int getId() {
        return id;
    }

    public int[] getStack() {
        return stack;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getFramePointer() {
        return framePointer;
    }

    public void setFramePointer(int framePointer) {
        this.framePointer = framePointer;
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }
}
