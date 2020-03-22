package asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AsmFormatTest {

  @Test
  public void test() {
    /*
     * 1P
     */
    Instruction[] instructions = new Instruction[] {
      new Add(),
      new And(),
      new Brc(42),
      new Call(10),
      new Cmp(CompareType.EQ),
      new Decl(5),
      new Div(),
      new Halt(),
      new In(),
      new Out(),
      new Ldi(10),
      new Lfs(3),
      new Mod(),
      new Mul(),
      new Nop(),
      new Not(),
      new Or(),
      new Pop(1),
      new Push(0),
      new Return(3),
      new Sts(2),
      new Sub(),
      new Cmp(CompareType.LT)
    };
    
    String expected =
      "0: ADD\n" + 
      "1: AND\n" + 
      "2: BRC 42\n" + 
      "3: CALL 10\n" + 
      "4: CMP EQUALS\n" + 
      "5: DECL 5\n" + 
      "6: DIV\n" + 
      "7: HALT\n" + 
      "8: IN\n" + 
      "9: OUT\n" + 
      "10: LDI 10\n" + 
      "11: LFS 3\n" + 
      "12: MOD\n" + 
      "13: MUL\n" + 
      "14: NOP\n" + 
      "15: NOT\n" + 
      "16: OR\n" + 
      "17: POP 1\n" + 
      "18: PUSH 0\n" + 
      "19: RETURN 3\n" + 
      "20: STS 2\n" + 
      "21: SUB\n" +
      "22: CMP LESS\n";
    
    AsmFormatVisitor afv = new AsmFormatVisitor(instructions);
    assertEquals(expected, afv.getFormattedCode());
  }

}
