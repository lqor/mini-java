package asm;

import static org.junit.Assert.*;

import org.junit.Test;

public class InterpreterTest {
  @Test
  public void testAllocZero() {
    /*
     * 0.5P
     */

    Instruction[] program = {
      new Ldi(0),
      new Alloc(),
      new Pop(0),
      new Ldi(1),
      new Alloc(),
      new Pop(1),
      new Push(0),
      new Push(1),
      new Cmp(CompareType.EQ),
      new Halt()
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(0, intp.execute());
  }
  
  @Test
  public void testHeapSimple() {
    /*
     * 0.5P
     */

    Instruction[] program = {
      new Ldi(2),
      new Alloc(),
      new Pop(0),
      new Ldi(42),
      new Push(0),
      new Sth(),
      new Push(0),
      new Lfh(),
      new Halt()
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(42, intp.execute());
  }
  
  @Test
  public void testExpr() {
    Instruction[] program = {
      new Ldi(5),
      new Ldi(2),
      new Mul(),
      new Ldi(3),
      new Mod(),
      new Not(),
      new Halt()
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(-4, intp.execute());
  }
  
  @Test
  public void testVariables() {
    Instruction[] program = {
      new Decl(3),
      new Ldi(5),
      new Sts(1),
      new Ldi(2),
      new Sts(2),
      new Ldi(3),
      new Sts(3),
      new Lfs(1),
      new Lfs(2),
      new Mul(),
      new Lfs(3),
      new Mod(),
      new Not(),
      new Halt()
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(-4, intp.execute());
  }
  
  @Test
  public void testControlFlow() {
    Instruction[] program = {
        new Decl(1),
        new Decl(1),
        new Decl(1),
        new Ldi(0),
        new Not(),
        new Ldi(7),
        new Ldi(3),
        new Cmp(CompareType.LT),
        new And(),
        new Brc(15),
        new Ldi(1),
        new Halt(),
        new Ldi(0),
        new Not(),
        new Brc(65),
        new Ldi(1),
        new Sts(1),
        new Ldi(2),
        new Sts(2),
        new Ldi(3),
        new Sts(3),
        new Ldi(1000),
        new Lfs(1),
        new Cmp(CompareType.LT),
        new Not(),
        new Brc(59),
        new Ldi(1),
        new Lfs(1),
        new Add(),
        new Sts(1),
        new Lfs(2),
        new Lfs(1),
        new Cmp(CompareType.LT),
        new Not(),
        new Not(),
        new Brc(56),
        new Ldi(1),
        new Lfs(2),
        new Add(),
        new Sts(2),
        new Lfs(3),
        new Lfs(1),
        new Cmp(CompareType.LT),
        new Not(),
        new Not(),
        new Brc(53),
        new Ldi(2),
        new Lfs(3),
        new Mul(),
        new Sts(3),
        new Ldi(0),
        new Not(),
        new Brc(40),
        new Ldi(0),
        new Not(),
        new Brc(30),
        new Ldi(0),
        new Not(),
        new Brc(21),
        new Lfs(3),
        new Lfs(2),
        new Add(),
        new Lfs(1),
        new Add(),
        new Halt()
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(3537, intp.execute());
  }

  @Test
  public void testFunctionCalls() {
    Instruction[] program = {
        new Ldi(26),
        new Call(0),
        new Halt(),
        new Lfs(-5),
        new Lfs(-4),
        new Lfs(-3),
        new Lfs(-2),
        new Lfs(-1),
        new Lfs(0),
        new Ldi(35),
        new Call(1),
        new Ldi(39),
        new Call(2),
        new Ldi(22),
        new Call(2),
        new Ldi(39),
        new Call(2),
        new Ldi(39),
        new Call(2),
        new Ldi(22),
        new Call(2),
        new Return(6),
        new Lfs(0),
        new Lfs(-1),
        new Add(),
        new Return(2),
        new Ldi(2),
        new Ldi(4),
        new Ldi(8),
        new Ldi(16),
        new Ldi(32),
        new Ldi(64),
        new Ldi(3),
        new Call(6),
        new Return(0),
        new Lfs(0),
        new Ldi(0),
        new Sub(),
        new Return(1),
        new Lfs(0),
        new Ldi(35),
        new Call(1),
        new Lfs(-1),
        new Add(),
        new Return(2),
    };
    Interpreter intp = new Interpreter(program);
    assertEquals(110, intp.execute());
  }

  @Test
  public void testFib() {
    Instruction[] program = {
        new Ldi(3),
        new Call(0),
        new Halt(),
        new Ldi(30),
        new Ldi(7),
        new Call(1),
        new Return(0),
        new Decl(4),
        new Lfs(0),
        new Ldi(1),
        new Cmp(CompareType.LT),
        new Not(),
        new Not(),
        new Brc(16),
        new Lfs(0),
        new Return(5),
        new Ldi(0),
        new Sts(1),
        new Ldi(1),
        new Sts(2),
        new Ldi(1),
        new Sts(3),
        new Lfs(0),
        new Lfs(3),
        new Cmp(CompareType.LT),
        new Not(),
        new Brc(44),
        new Lfs(1),
        new Sts(4),
        new Lfs(2),
        new Lfs(1),
        new Add(),
        new Sts(1),
        new Lfs(4),
        new Not(),
        new Not(),
        new Sts(2),
        new Ldi(1),
        new Lfs(3),
        new Add(),
        new Sts(3),
        new Ldi(0),
        new Not(),
        new Brc(22),
        new Ldi(2),
        new Lfs(0),
        new Sub(),
        new Ldi(7),
        new Call(1),
        new Lfs(1),
        new Add(),
        new Return(5),
    };
    
    Interpreter intp = new Interpreter(program);
    assertEquals(832040, intp.execute());
  }

}
