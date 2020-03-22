package asm;

public interface AsmVisitor {
  void visit(Add add);

  void visit(Decl decl);

  void visit(And and);

  void visit(Brc brc);

  void visit(Call call);

  void visit(Cmp cmp);

  void visit(Div div);

  void visit(Halt halt);

  void visit(Ldi ldi);

  void visit(Lfs lfs);

  void visit(Mod mod);

  void visit(Mul mul);

  void visit(Nop nop);

  void visit(Not not);

  void visit(Or or);

  void visit(Pop pop);

  void visit(Push push);

  void visit(In in);

  void visit(Return ret);

  void visit(Sts sts);

  void visit(Sub sub);

  void visit(Out out);

  void visit(Lfh lfh);
  
  void visit(Sth sth);

  void visit(Alloc alloc);

  void visit(Fork fork);
  void visit(Join join);
  void visit(Lock lock);
  void visit(Unlock unlock);
}
