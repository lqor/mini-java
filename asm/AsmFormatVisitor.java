package asm;

public class AsmFormatVisitor implements AsmVisitor {
  private StringBuilder formattedCode = new StringBuilder();
  
  public String getFormattedCode() {
    return formattedCode.toString();
  }
  
  public AsmFormatVisitor(Instruction[] instructions) {
    for (int i = 0; i < instructions.length; i++) {
      formattedCode.append(i + ": ");
      instructions[i].accept(this);
      formattedCode.append('\n');
    }
  }

  @Override
  public void visit(Fork fork) {
    formattedCode.append(fork.toString());
  }

  @Override
  public void visit(Join join) {
    formattedCode.append(join.toString());
  }

  @Override
  public void visit(Lock lock) {
    formattedCode.append(lock.toString());
  }

  @Override
  public void visit(Unlock unlock) {
    formattedCode.append(unlock.toString());
  }

  @Override
  public void visit(Add add) {
    formattedCode.append(add);
  }

  @Override
  public void visit(Decl decl) {
    formattedCode.append(decl);
    
  }

  @Override
  public void visit(And and) {
    formattedCode.append(and);
    
  }

  @Override
  public void visit(Brc brc) {
    formattedCode.append(brc);
    
  }

  @Override
  public void visit(Call call) {
    formattedCode.append(call);
    
  }

  @Override
  public void visit(Cmp cmp) {
    formattedCode.append(cmp);
    
  }

  @Override
  public void visit(Div div) {
    formattedCode.append(div);
    
  }

  @Override
  public void visit(Halt halt) {
    formattedCode.append(halt);
    
  }

  @Override
  public void visit(Ldi ldi) {
    formattedCode.append(ldi);
    
  }

  @Override
  public void visit(Lfs lds) {
    formattedCode.append(lds);
    
  }

  @Override
  public void visit(Mod mod) {
    formattedCode.append(mod);
    
  }

  @Override
  public void visit(Mul mul) {
    formattedCode.append(mul);
    
  }

  @Override
  public void visit(Nop nop) {
    formattedCode.append(nop);
  }

  @Override
  public void visit(Not not) {
    formattedCode.append(not);
    
  }

  @Override
  public void visit(Or or) {
    formattedCode.append(or);
    
  }

  @Override
  public void visit(Pop pop) {
    formattedCode.append(pop);
    
  }

  @Override
  public void visit(Push push) {
    formattedCode.append(push);
    
  }

  @Override
  public void visit(In read) {
    formattedCode.append(read);
    
  }

  @Override
  public void visit(Return ret) {
    formattedCode.append(ret);
    
  }

  @Override
  public void visit(Sts sts) {
    formattedCode.append(sts);
    
  }

  @Override
  public void visit(Sub sub) {
    formattedCode.append(sub);
    
  }

  @Override
  public void visit(Out write) {
    formattedCode.append(write);
    
  }

  @Override
  public void visit(Lfh lfh) {
    formattedCode.append(lfh);
  }

  @Override
  public void visit(Sth sth) {
    formattedCode.append(sth);
  }

  @Override
  public void visit(Alloc alloc) {
    formattedCode.append(alloc);
  }
  
  
}
