package codegen;

public interface ProgramVisitor {
  void visit(Number number);

  void visit(Variable nameExpression);

  void visit(Unary unary);

  void visit(Binary binary);

  void visit(Function function);

  void visit(Declaration declaration);

  void visit(Read read);

  void visit(Assignment assignment);

  void visit(Composite composite);

  void visit(Comparison comparison);

  void visit(While while_);

  void visit(True true_);

  void visit(False false_);

  void visit(UnaryCondition unaryCondition);

  void visit(BinaryCondition binaryCondition);

  void visit(IfThenElse ifThenElse);

  void visit(Program program);

  void visit(IfThen ifThen);

  void visit(Call call);

  void visit(Return return_);

  void visit(Write write);

  void visit(EmptyStatement emptyStatement);

  void visit(ExpressionStatement exprStmt);

  void visit(Switch sw);
  
  void visit(Break br);
  
  void visit(ArrayAccess access);

  void visit(ArrayAllocator alloc);

  void visit(ArrayIndexAssignment arrayAss);

  void visit(ArrayLength arrayLength);

  void visit(Synchronized synch);
  void visit(Join join);
}
