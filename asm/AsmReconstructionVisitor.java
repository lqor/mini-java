package asm;

public class AsmReconstructionVisitor implements AsmVisitor {
  private StringBuilder formattedCode = new StringBuilder();
  
  public String getFormattedCode() {
    return formattedCode.toString();
  }
  
  public AsmReconstructionVisitor(Instruction[] instructions) {
    for (int i = 0; i < instructions.length; i++) {
      formattedCode.append("  ");
      instructions[i].accept(this);
      formattedCode.append(",\n");
    }
  }

  @Override
  public void visit(Fork fork) {
    formattedCode.append("new Fork("+fork.getAddress()+");");
  }

  @Override
  public void visit(Join join) {
    formattedCode.append("new Join();");
  }

  @Override
  public void visit(Lock lock) {
    formattedCode.append("new Lock();");
  }

  @Override
  public void visit(Unlock unlock) {
    formattedCode.append("new Unlock();");
  }

  @Override
  public void visit(Add add) {
    formattedCode.append("new Add()");
  }

  @Override
  public void visit(Decl decl) {
    formattedCode.append("new Decl(" + decl.getCount() + ")");
  }

  @Override
  public void visit(And and) {
    formattedCode.append("new And()");
  }

  @Override
  public void visit(Brc brc) {
    formattedCode.append("new Brc(" + brc.getTarget() + ")");
  }

  @Override
  public void visit(Call call) {
    formattedCode.append("new Call(" + call.getArgCount() + ")");
  }

  @Override
  public void visit(Cmp cmp) {
    switch(cmp.getCompareType()) {
      case EQ:
        formattedCode.append("new Cmp(CompareType.EQ)");
        break;
      case LT:
        formattedCode.append("new Cmp(CompareType.LT)");
        break;
    }
  }

  @Override
  public void visit(Div div) {
    formattedCode.append("new Div()");
  }

  @Override
  public void visit(Halt halt) {
    formattedCode.append("new Halt()");
  }

  @Override
  public void visit(Ldi ldi) {
    formattedCode.append("new Ldi(" + ldi.getValue() + ")");
  }

  @Override
  public void visit(Lfs lfs) {
    formattedCode.append("new Lfs(" + lfs.getIndex() + ")");
    
  }

  @Override
  public void visit(Mod mod) {
    formattedCode.append("new Mod()");
  }

  @Override
  public void visit(Mul mul) {
    formattedCode.append("new Mul()");
  }

  @Override
  public void visit(Nop nop) {
    formattedCode.append("new Nop()");
  }

  @Override
  public void visit(Not not) {
    formattedCode.append("new Not()");
  }

  @Override
  public void visit(Or or) {
    formattedCode.append("new Or()");
  }

  @Override
  public void visit(Pop pop) {
    formattedCode.append("new Pop(" + pop.getRegister() + ")");
  }

  @Override
  public void visit(Push push) {
    formattedCode.append("new Push(" + push.getRegister() + ")");
  }

  @Override
  public void visit(In in) {
    formattedCode.append("new In()");
  }

  @Override
  public void visit(Return ret) {
    formattedCode.append("new Return(" + ret.getCells() + ")");
  }

  @Override
  public void visit(Sts sts) {
    formattedCode.append("new Sts(" + sts.getIndex() + ")");
  }

  @Override
  public void visit(Sub sub) {
    formattedCode.append("new Sub()");
  }

  @Override
  public void visit(Out out) {
    formattedCode.append("new Out()");
  }

  @Override
  public void visit(Lfh lfh) {
    formattedCode.append("new Lfh()");
    
  }

  @Override
  public void visit(Sth sth) {
    formattedCode.append("new Sth()");
    
  }

  @Override
  public void visit(Alloc alloc) {
    formattedCode.append("new Alloc()");
  }
}
