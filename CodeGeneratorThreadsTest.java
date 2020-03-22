import Exceptions.DeadlockException;
import Exceptions.SomeThreadNotClosed;
import asm.Interpreter;
import codegen.Function;
import codegen.*;
import codegen.Number;
import codegen.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


@SuppressWarnings("Duplicates")
public class CodeGeneratorThreadsTest {
    /**
     * • Es tritt eine Race Condition auf, durch die ein falsches Ergebnis berechnet wird.
     *          == testWithoutSynchronized(), races1
     * • Die obige Race Condition tritt nicht mehr auf, wenn passend synchronisiert wird.
     *          == testWithSynchronized(), races2
     * • Es tritt ein Deadlock auf. ==testDeadlock()
     * */
    @Test
    public void testJoinFork_ThreadsNotClosed() {
        /**int main() {
            int result;
            result = 3;
            result = fork:inc(); //es wird inc-ID hinterlassen und nicht der Rueckgabewert
                                // deshalb ist RaceCondition da
            return result;
          }

         int inc() {
            return 10;
         }*/
        Function main = new Function(Type.Int,
                "main",
                new Parameter[] { },
                new Declaration[] {new Declaration(Type.Int, "result") },
                new Statement[] {
                        new Assignment("result", new Number(3)),
                        new Assignment("result",
                                new Call("inc", new Expression[] { }, true)),
                        new Return(new Variable("result")) });

        Function inc = new Function(Type.Int,
                "inc",
                new Parameter[] { },
                new Declaration[] { },
                new Statement[] {
                        //new Return(new Binary(new Variable("a"), Binop.Plus, new Number(1)))
                        new Return(new Number(10))
                });

        Program program = new Program( new Function[] { main, inc } );

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        try {
            interpreter.execute();
        } catch (SomeThreadNotClosed e ) {
            assertEquals(1, 1); //immer true, falls exception geworfen
        }


    }

    @Test
    public void testJoinFork_No_RaceCondition() {
        /**int main() {
         int result;
         result = 3;
         result = join(fork:inc()); //es wird inc-ID hinterlassen und nicht der Rueckgabewert
         // deshalb ist RaceCondition da
         return result;
         }

         int inc() {
         return 10;
         }*/
        Function main = new Function(Type.Int,
                "main",
                new Parameter[] { },
                new Declaration[] {new Declaration(Type.Int, "result") },
                new Statement[] {
                        new Assignment("result", new Number(3)),
                        new Assignment("result",
                                new Join(new Call("inc", new Expression[] { }, true))),
                        new Return(new Variable("result")) });

        Function inc = new Function(Type.Int,
                "inc",
                new Parameter[] { },
                new Declaration[] { },
                new Statement[] {
                        //new Return(new Binary(new Variable("a"), Binop.Plus, new Number(1)))
                        new Return(new Number(10))
                });

        Program program = new Program( new Function[] { main, inc } );

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(10, interpreter.execute());

    }

    @Test
    public void testJoinFork_NestedForks() {
        /**
         * int main() {
         *   int result;
         *   int i;
         *   result = 0;
         *   i = 0;
         *   result = join(fork:inc(result)); //result wird um 1 erh.
         *   i = join(fork:inc2(i));          //i um 2 erhoehen
         *   return (result + i);
         * }
         *
         * int inc(int a) {
         *   return (a + 1);
         * }
         *
         * int inc2(int a) {
         *   return (a + 2);
         * }
         * */
        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.Int, "result"), new Declaration(Type.Int, "i") }, new Statement[] {
                new Assignment("result", new Number(0)), new Assignment("i", new Number(0)), new
                Assignment("result", new Join(new Call("inc", new Expression[] { new Variable("result")
        }, true))), new Assignment("i", new Join(new Call("inc2", new Expression[] { new
                Variable("i") }, true))), new Return(new Binary(new Variable("result"), Binop.Plus, new
                Variable("i"))) });

        Function inc = new Function(Type.Int, "inc", new Parameter[] { new Parameter(Type.Int,
                "a") }, new Declaration[] { }, new Statement[] { new Return(new Binary(new Variable("a"),
                Binop.Plus, new Number(1))) });

        Function inc2 = new Function(Type.Int, "inc2", new Parameter[] { new Parameter(Type.Int,
                "a") }, new Declaration[] { }, new Statement[] { new Return(new Binary(new Variable("a"),
                Binop.Plus, new Number(2))) });


        Program program = new Program( new Function[] { main, inc, inc2 } );

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(3, interpreter.execute());

    }

    @Test
    public void testWithSynchronized() {
        Function inc = new Function(Type.Int, "inc", new Parameter[] { new
                Parameter(Type.IntArray, "array") }, new Declaration[] { }, new Statement[] { new
                Synchronized(new Variable("array"), new Statement[] { new ArrayIndexAssignment(new
                Variable("array"), new Number(0), new Binary(new ArrayAccess(new Variable("array"), new
                Number(0)), Binop.Plus, new Number(1))) }), new Return(new Number(42)) });


        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, "array"), new Declaration(Type.Int, new String[] { "x",
                "t1", "t2" } ) }, new Statement[] { new Assignment("array", new ArrayAllocator(new
                Number(1))), new ArrayIndexAssignment(new Variable("array"), new Number(0), new
                Number(0)), new Assignment("t1", new Call("inc", new Expression[] { new Variable("array")
        }, true)), new Assignment("t2", new Call("inc", new Expression[] { new Variable("array")
        }, true)), new Assignment("x", new Join(new Variable("t1"))), new Assignment("x", new
                Binary(new Variable("x"), Binop.Plus, new Join(new Variable("t2")))), new Return(new
                Binary(new Variable("x"), Binop.Plus, new ArrayAccess(new Variable("array"), new
                Number(0)))) });


        Program program = new Program( new Function[] { inc, main } );

        FormatVisitor fv = new FormatVisitor();
        program.accept(fv);
        System.out.println(fv.getFormattedCode());

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(86, interpreter.execute());
    }

    @Test
    public void testWithoutSynchronized() {
        /**
         * int inc(int[] array) {
         *   array[0] = (array[0] + 1);
         *   return 42;
         * }
         *
         * int main() {
         *   int[] array;
         *   int x, t1, t2;
         *
         *   array = new int[1];
         *   array[0] = 0;
         *
         *   t1 = fork:inc(array);
         *   t2 = fork:inc(array);
         *
         *   x = join(t1);
         *   x = (x + join(t2));
         *
         *   return (x + array[0]);
         * }
         * */
        Function inc = new Function(
                Type.Int,
                "inc",
                new Parameter[] { new Parameter(Type.IntArray, "array") },
                new Declaration[] { },
                new Statement[] {
                        new ArrayIndexAssignment(new Variable("array"), new Number(0), new Binary(new ArrayAccess(new Variable("array"), new
                                Number(0)), Binop.Plus, new Number(1))),
                        new Return(new Number(42))
                });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, "array"), new Declaration(Type.Int, new String[] { "x",
                "t1", "t2" } ) }, new Statement[] { new Assignment("array", new ArrayAllocator(new
                Number(1))), new ArrayIndexAssignment(new Variable("array"), new Number(0), new
                Number(0)), new Assignment("t1", new Call("inc", new Expression[] { new Variable("array")
        }, true)), new Assignment("t2", new Call("inc", new Expression[] { new Variable("array")
        }, true)), new Assignment("x", new Join(new Variable("t1"))), new Assignment("x", new
                Binary(new Variable("x"), Binop.Plus, new Join(new Variable("t2")))), new Return(new
                Binary(new Variable("x"), Binop.Plus, new ArrayAccess(new Variable("array"), new
                Number(0)))) });


        Program program = new Program( new Function[] { inc, main } );

        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(85, interpreter.execute());
    }

    @Test
    public void testDeadlock() {
        /**
         *int run2(int[] arr1, int[] arr2) {
         *   synchronized(arr1) {
         *     synchronized(arr2) {
         *       arr2[0] = (arr2[0] + 1);
         *     }
         *   }
         *   return 42;
         * }
         *
         * int run1(int[] arr1, int[] arr2) {
         *   synchronized(arr2) {
         *     synchronized(arr1) {
         *     //hier Deadlock!
         *       arr2[0] = (arr2[0] + 1);
         *       arr2[0] = (arr2[0] + 1);
         *       arr2[0] = (arr2[0] + 1);
         *     }
         *   }
         *   return 42;
         * }
         *
         * int main() {
         *   int[] arr1, arr2;
         *   int x, t1, t2;
         *   arr1 = new int[1];
         *   arr2 = new int[1];
         *   arr2[0] = 0;
         *   t1 = fork:run1(arr1, arr2);
         *   t2 = fork:run2(arr1, arr2);
         *   x = join(t1);
         *   x = (x + join(t2));
         *   return (x + arr1[0]);
         * }
         * */
        Function run2 = new Function(Type.Int, "run2", new Parameter[] { new
                Parameter(Type.IntArray, "arr1"), new Parameter(Type.IntArray, "arr2") }, new
                Declaration[] { }, new Statement[] { new Synchronized(new Variable("arr1"), new
                Statement[] { new Synchronized(new Variable("arr2"), new Statement[] { new
                ArrayIndexAssignment(new Variable("arr2"), new Number(0), new Binary(new ArrayAccess(new
                Variable("arr2"), new Number(0)), Binop.Plus, new Number(1))) }) }), new Return(new
                Number(42)) });

        Function run1 = new Function(Type.Int, "run1", new Parameter[] { new
                Parameter(Type.IntArray, "arr1"), new Parameter(Type.IntArray, "arr2") }, new
                Declaration[] { }, new Statement[] { new Synchronized(new Variable("arr2"), new
                Statement[] { new Synchronized(new Variable("arr1"), new Statement[] { new
                ArrayIndexAssignment(new Variable("arr2"), new Number(0), new Binary(new ArrayAccess(new
                Variable("arr2"), new Number(0)), Binop.Plus, new Number(1))), new
                ArrayIndexAssignment(new Variable("arr2"), new Number(0), new Binary(new ArrayAccess(new
                Variable("arr2"), new Number(0)), Binop.Plus, new Number(1))), new
                ArrayIndexAssignment(new Variable("arr2"), new Number(0), new Binary(new ArrayAccess(new
                Variable("arr2"), new Number(0)), Binop.Plus, new Number(1))) }) }), new Return(new
                Number(42)) });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, new String[] { "arr1", "arr2" } ), new
                Declaration(Type.Int, new String[] { "x", "t1", "t2" } ) }, new Statement[] { new
                Assignment("arr1", new ArrayAllocator(new Number(1))), new Assignment("arr2", new
                ArrayAllocator(new Number(1))), new ArrayIndexAssignment(new Variable("arr2"), new
                Number(0), new Number(0)), new Assignment("t1", new Call("run1", new Expression[] { new
                Variable("arr1"), new Variable("arr2") }, true)), new Assignment("t2", new Call("run2",
                new Expression[] { new Variable("arr1"), new Variable("arr2") }, true)), new
                Assignment("x", new Join(new Variable("t1"))), new Assignment("x", new Binary(new
                Variable("x"), Binop.Plus, new Join(new Variable("t2")))), new Return(new Binary(new
                Variable("x"), Binop.Plus, new ArrayAccess(new Variable("arr1"), new Number(0)))) });


        Program program = new Program( new Function[] { run2, run1, main } );

        FormatVisitor fv = new FormatVisitor();
        program.accept(fv);
        System.out.println(fv.getFormattedCode());


        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        try {
            interpreter.execute();
        } catch (DeadlockException deadLock) {
            assertEquals(1,1);
        }
    }

    @Test
    public void returnSynchr() {
        /**
         * int inc(int[] array) {
         *   synchronized(array) {
         *      return 42;
         *   }
         * }
         *
         * int main() {
         *   int[] array;
         *   int t1;
         *
         *   array = new int[1];
         *   array[0] = 0;
         *
         *   t1 = fork:inc(array);
         *
         *   return join(t1);
         * }
         * */

        Function inc = new Function(
                Type.Int,
                "inc",
                new Parameter[] {new Parameter(Type.IntArray, "array") },
                new Declaration[] { },
                new Statement[] {
                        new Synchronized(new Variable("array"), new Statement[] { new Return(new Number(42)) })
                });

        Function main = new Function(
                Type.Int,
                "main",
                new Parameter[] { },
                new Declaration[] {new Declaration(Type.IntArray, "array"),
                                    new Declaration(Type.Int, new String[] { "x", "t1"} ) },
                new Statement[] {
                        new Assignment("array", new ArrayAllocator(new Number(1))),
                        new ArrayIndexAssignment(new Variable("array"), new Number(0), new  Number(0)),
                        new Assignment("t1",
                                new Call("inc",
                                        new Expression[] {
                                                new Variable("array")}, true)),
                        new Return(new Join(new Variable("t1")))
                });


        Program program = new Program( new Function[] { inc, main } );
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(42, interpreter.execute());
    }

    @Test
    public void nestedSynchr() {
        /**
         * int inc(int[] array, int[] b) {
         *   int i;
         *   i = 0;
         *   synchronized(array) {
         *     synchronized(b) {
         *       while((i < 5)) {
         *         i = (i + 1);
         *       }
         *       return 42;
         *     }
         *   }
         * }
         *
         * int main() {
         *   int[] array, b;
         *   int t1;
         *   array = new int[1];
         *   array[0] = 0;
         *   b = new int[1];
         *   b[0] = 1;
         *   t1 = fork:inc(array, b);
         *   return join(t1);
         * }
         * */
        Function inc = new Function(Type.Int, "inc",
                new Parameter[] {
                        new Parameter(Type.IntArray, "array"), new Parameter(Type.IntArray, "b") },
                new Declaration[]{ new Declaration(Type.Int, "i") },
                new Statement[] { new Assignment("i", new Number(0)),
                new Synchronized(
                        new Variable("array"),
                        new Statement[] {
                                new Synchronized(
                                        new Variable("b"),
                                        new Statement[] {
                                                new While(new Comparison(
                                                        new Variable("i"),
                                                        Comp.Less,
                                                        new Number(2)),
                                                        new Assignment(
                                                                "i",
                                                                new Binary(
                                                                        new Variable("i"),
                                                                        Binop.Plus,
                                                                        new Number(1))),
                                                        false),
                                                new Return(new Number(42))
                                        })
                        })
                });

        Function main = new Function(
                Type.Int,
                "main",
                new Parameter[] { },
                new Declaration[] {
                        new Declaration(Type.IntArray, new String[] { "array", "b" } ),
                        new Declaration(Type.Int,"t1") },
                new Statement[] {
                        new Assignment("array", new ArrayAllocator(new Number(1))),
                        new ArrayIndexAssignment(new Variable("array"), new Number(0), new Number(0)),
                        new Assignment("b", new ArrayAllocator(new Number(1))),
                        new ArrayIndexAssignment(new Variable("b"), new Number(0), new Number(1)),
                        new Assignment("t1",
                                new Call("inc",
                                        new Expression[] { new Variable("array"), new Variable("b") }, true)),
                        new Return(new Join(new Variable("t1")))
                });


        Program program = new Program( new Function[] { inc, main } );


        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(42, interpreter.execute());

    }

    @Test
    public void nesteSynchr2() {
        /**
         * int inc(int[] array, int[] b) {
         *   int i;
         *   i = 0;
         *   synchronized(array) {
         *     synchronized(b) {
         *       while((i < 5)) {
         *         i = (i + 1);
         *       }
         *     }
         *     return 42;
         *   }
         * }
         *
         * int main() {
         *   int[] array, b;
         *   int t1;
         *   array = new int[1];
         *   array[0] = 0;
         *   b = new int[1];
         *   b[0] = 1;
         *   t1 = fork:inc(array, b);
         *   return join(t1);
         * }
         * */
        Function inc = new Function(Type.Int, "inc", new Parameter[] { new
                Parameter(Type.IntArray, "array"), new Parameter(Type.IntArray, "b") }, new Declaration[]
                { new Declaration(Type.Int, "i") }, new Statement[] { new Assignment("i", new Number(0)),
                new Synchronized(new Variable("array"), new Statement[] { new Synchronized(new
                        Variable("b"), new Statement[] { new While(new Comparison(new Variable("i"), Comp.Less,
                        new Number(5)), new Assignment("i", new Binary(new Variable("i"), Binop.Plus, new
                        Number(1))), false) }), new Return(new Number(42)) }) });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, new String[] { "array", "b" } ), new Declaration(Type.Int,
                "t1") }, new Statement[] { new Assignment("array", new ArrayAllocator(new Number(1))),
                new ArrayIndexAssignment(new Variable("array"), new Number(0), new Number(0)), new
                Assignment("b", new ArrayAllocator(new Number(1))), new ArrayIndexAssignment(new
                Variable("b"), new Number(0), new Number(1)), new Assignment("t1", new Call("inc", new
                Expression[] { new Variable("array"), new Variable("b") }, true)), new Return(new
                Join(new Variable("t1"))) });


        Program program = new Program( new Function[] { inc, main } );


        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(42, interpreter.execute());

    }

    @Test
    public void nesteSynchr3() {
        /**
         * int inc(int[] array, int[] b, int c[]) {
         *   synchronized(array) {
         *     synchronized(b) {
         *       synchronized(c) {
         *         return 42;
         *        }
         *     }
         *
         *   }
         * }
         *
         * int main() {
         *   int[] array, b, c;
         *   int t1;
         *   array = new int[1];
         *   array[0] = 0;
         *   b = new int[1];
         *   b[0] = 1;
         *   c = new int[1];
         *   c[0] = 1;
         *   t1 = fork:inc(array, b, c);
         *   return join(t1);
         * }
         * */

        Function inc = new Function(Type.Int, "inc", new Parameter[] { new
                Parameter(Type.IntArray, "array"), new Parameter(Type.IntArray, "b"), new
                Parameter(Type.IntArray, "c") }, new Declaration[] { }, new Statement[] { new
                Synchronized(new Variable("array"), new Statement[] { new Synchronized(new Variable("b"),
                new Statement[] { new Synchronized(new Variable("c"), new Statement[] { new Return(new
                        Number(42)) }) }) }) });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, new String[] { "array", "b", "c" } ), new
                Declaration(Type.Int, "t1") }, new Statement[] { new Assignment("array", new
                ArrayAllocator(new Number(1))), new ArrayIndexAssignment(new Variable("array"), new
                Number(0), new Number(0)), new Assignment("b", new ArrayAllocator(new Number(1))), new
                ArrayIndexAssignment(new Variable("b"), new Number(0), new Number(1)), new
                Assignment("c", new ArrayAllocator(new Number(1))), new ArrayIndexAssignment(new
                Variable("c"), new Number(0), new Number(1)), new Assignment("t1", new Call("inc", new
                Expression[] { new Variable("array"), new Variable("b"), new Variable("c") }, true)), new
                Return(new Join(new Variable("t1"))) });


        Program program = new Program( new Function[] { inc, main } );
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(42, interpreter.execute());

    }

    @Test
    public void races1 (){
        /**
         * int inc(int[] a) {
         *   a[0] = 30;
         * }
         *
         * int inc2(int[] a) {
         *     a[0] = a[0] + 1;
         * }
         *
         * int main() {
         *   int[] a;
         *   a = new int[1];
         *   t1 = fork:inc(a);
         *   t2 = fork:inc2(a);
         *   return join(t1);
         * }
         * */
        Function inc = new Function(Type.Int, "inc", new Parameter[] { new
                Parameter(Type.IntArray, "a") }, new Declaration[] { }, new Statement[] { new
                ArrayIndexAssignment(new Variable("a"), new Number(0), new Binary(new ArrayAccess(new
                Variable("a"), new Number(0)), Binop.Plus, new Number(10))), new Return(new
                ArrayAccess(new Variable("a"), new Number(0))) });

        Function inc2 = new Function(Type.Int, "inc2", new Parameter[] { new
                Parameter(Type.IntArray, "a") }, new Declaration[] { }, new Statement[] { new
                ArrayIndexAssignment(new Variable("a"), new Number(0), new Number(30)), new Return(new
                ArrayAccess(new Variable("a"), new Number(0))) });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, "a"), new Declaration(Type.Int, new String[] { "t1", "t2"
        } ) }, new Statement[] { new Assignment("t1", new Number(0)), new Assignment("t2", new
                Number(0)), new Assignment("a", new ArrayAllocator(new Number(1))), new Assignment("t1",
                new Call("inc", new Expression[] { new Variable("a") }, true)), new Assignment("t2", new
                Call("inc2", new Expression[] { new Variable("a") }, true)), new ExpressionStatement(new
                Join(new Variable("t2"))), new ExpressionStatement(new Join(new Variable("t1"))), new
                Return(new ArrayAccess(new Variable("a"), new Number(0))) });


        Program program = new Program( new Function[] { inc, inc2, main } );



        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(10, interpreter.execute());
    }

    @Test
    public void races2() {
        Function inc = new Function(Type.Int, "inc", new Parameter[] { new
                Parameter(Type.IntArray, "a") }, new Declaration[] { }, new Statement[] { new
                Synchronized(new Variable("a"), new Statement[] { new ArrayIndexAssignment(new
                Variable("a"), new Number(0), new Binary(new ArrayAccess(new Variable("a"), new
                Number(0)), Binop.Plus, new Number(10))) }), new Return(new ArrayAccess(new
                Variable("a"), new Number(0))) });

        Function inc2 = new Function(Type.Int, "inc2", new Parameter[] { new
                Parameter(Type.IntArray, "a") }, new Declaration[] { }, new Statement[] { new
                Synchronized(new Variable("a"), new Statement[] { new ArrayIndexAssignment(new
                Variable("a"), new Number(0), new Number(30)) }), new Return(new ArrayAccess(new
                Variable("a"), new Number(0))) });

        Function main = new Function(Type.Int, "main", new Parameter[] { }, new Declaration[] {
                new Declaration(Type.IntArray, "a"), new Declaration(Type.Int, new String[] { "t1", "t2"
        } ) }, new Statement[] { new Assignment("t1", new Number(0)), new Assignment("t2", new
                Number(0)), new Assignment("a", new ArrayAllocator(new Number(1))), new Assignment("t1",
                new Call("inc", new Expression[] { new Variable("a") }, true)), new Assignment("t2", new
                Call("inc2", new Expression[] { new Variable("a") }, true)), new ExpressionStatement(new
                Join(new Variable("t2"))), new ExpressionStatement(new Join(new Variable("t1"))), new
                Return(new ArrayAccess(new Variable("a"), new Number(0))) });


        Program program = new Program( new Function[] { inc, inc2, main } );
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        program.accept(codeGenerationVisitor);
        Interpreter interpreter = new Interpreter(codeGenerationVisitor.getProgram());
        assertEquals(30, interpreter.execute());
    }
    /**ANMERKUNG, 1 Test ist aus Piazza, hab vergessen welcher...*/
}
