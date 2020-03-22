import asm.*;
import Exceptions.SomeThreadNotClosed;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class InterpreterThreadsTest {
    @Test
    public void testForkJoinNoRaceCondition() {
        //es werden Intsruction zwischen Fork und Join hinzugefügt, dabei wird auch Wechsel nach 5
        // Instr. getestet und dass es kein RaceCondition auftritt
        Interpreter interpreter = new Interpreter(
                new Instruction[] {
                        new Ldi(10),
                        new Fork(0),
                        new Ldi(3),
                        new Out(),
                        new Ldi(3),
                        new Out(),
                        new Ldi(3),
                        new Out(),
                        new Join(),
                        new Halt(),
                        new Ldi(5), //10
                        new Ldi(4),
                        new Ldi(18), //
                        new Fork(0),
                        new Join(),
                        new Add(),
                        new Add(),
                        new Return(0),
                        new Ldi(-1), // 18
                        new Ldi(3),
                        new Add(),
                        new Return(0)
                }
        );


        assertEquals(11, interpreter.execute());
    }

    @Test
    public void testForkWithCalls() {
        Instruction[] program = {
                new Ldi(27),
                new Fork(0),
                new Join(),
                new Halt(),
                new Lfs(-5),
                new Lfs(-4),
                new Lfs(-3), //6
                new Lfs(-2), //7
                new Lfs(-1), //8
                new Lfs(0),
                new Ldi(36),
                new Call(1), //11
                new Ldi(40),
                new Call(2), //13
                new Ldi(23),
                new Call(2), //15
                new Ldi(40),
                new Call(2), //17
                new Ldi(40),
                new Call(2), //19
                new Ldi(23),
                new Call(2), //21
                new Return(6),
                new Lfs(0),
                new Lfs(-1),
                new Add(), //24
                new Return(2),
                new Ldi(2), //27
                new Ldi(4),
                new Ldi(8), //29
                new Ldi(16),
                new Ldi(32), //31
                new Ldi(64),
                new Ldi(4),
                new Call(6),
                new Return(0),
                new Lfs(0), //36
                new Ldi(0),
                new Sub(),
                new Return(1),
                new Lfs(0), //40
                new Ldi(36),
                new Call(1),
                new Lfs(-1), //43
                new Add(),
                new Return(2), //45
        };
        Interpreter intp = new Interpreter(program);
        assertEquals(110, intp.execute());
    }

    @Test
    public void testNestedForkJoin() {
        Interpreter interpreter = new Interpreter(
                new Instruction[]{
                        new Decl(2),
                        new Ldi(123),  //P1
                        new Ldi(456),  //P2
                        new Ldi(11),   //Adresse von f1 laden
                        new Fork(2),   //f1 in neuem Thread starten
                        new Sts(1),    //Thread ID in t1 speichern
                        new Lfs(1),    //Thread ID aus t1 laden
                        new Join(),    //Warten, bis der Thread fertig ist und Ergebnis geben lassen
                        new Sts(2),    //Ergebnis in result speichern
                        new Lfs(2),    //Ergebnis aus result laden
                        new Halt(),    //Fertig!

                        new Ldi(7),
                        new Ldi(30),
                        new Add(),     //(2 + 7) berechnen
                        new Lfs(-1),   //P1 (a laden)
                        new Lfs(0),    //P2 (b laden)
                        new Ldi(7),    //P3
                        new Ldi(22),   //Adresse von f2
                        new Fork(3),   //f2 in neuem Thread starten
                        new Join(),    //Thread ID liegt oben => wir können direkt warten
                        new Add(),     //Jetzt liegt der Returnwert oben und das Ergebnis von (2 + 7) direkt drunter
                        new Return(2), //f1 ist fertig

                        //Start f2
                        new Lfs(-2),
                        new Lfs(-1),
                        new Add(),
                        new Lfs(0),
                        new Add(),     //alle drei Werte addieren...
                        new Return(3)  //und fertig.
                }
        );
        assertEquals(623, interpreter.execute());
    }

    @Test
    public void testLock() {
        Interpreter interpreter = new Interpreter(
                new Instruction[] {
                        new Decl(4),
                        new Nop(), // Beginn von main()
                        new Ldi(1),
                        new Alloc(), // Array der Größe 1 anlegen
                        new Sts(1),
                        new Ldi(0),
                        new Lfs(1),
                        new Sth(), // 0 an array[0] speichern
                        new Lfs(1), // Array-Adresse auf den Stack laden
                        new Ldi(31), // Adresse von Methode inc laden
                        new Fork(1), // inc() in neuem Thread aufrufen
                        new Sts(3), // Thread-ID in Variable 3 (t1) speichern
                        new Lfs(1), // Array-Adresse auf den Stack laden
                        new Ldi(31), // Adresse von Methdoe inc laden
                        new Fork(1), // inc() in neuem Thread aufrufen
                        new Sts(4), // Thread-ID in Variable 4 (t2) speichern
                        new Lfs(3), // Thread-ID von erstem Thread laden
                        new Join(), // Auf Beendigung des ersten Thread warten
                        new Sts(2), // Rückgabewert von Thread (42) in Variable 2 (x) speichern
                        new Lfs(4), // Thread-ID von zweitem Thread laden
                        new Join(), // Auf Beendigung des zweiten Thread warten
                        new Lfs(2), // Variable 2 (x) laden
                        new Add(),
                        new Sts(2), // Summe der Rückgabewerte der Thread in Variable 2 (x) speichern
                        new Lfs(1),
                        new Lfh(), // Wert von array[0] auf den Stack laden
                        new Lfs(2), // Wert von Variable 2 (x) laden
                        new Add(),
                        new Halt(),
                        new Nop(),
                        new Nop(), // Beginn von inc()
                        new Lfs(0), // Heap-Objekt (Array) laden
                        new Lock(), // Lock akquirieren
                        new Ldi(1), // 1 laden
                        new Lfs(0), // Array-Adresse laden
                        new Lfh(), // Wert von array[0] laden
                        new Add(),
                        new Lfs(0),
                        new Sth(), // Summe an array[0] speichern
                        new Lfs(0), // Heap-Objekt (Array) laden
                        new Unlock(), // Mutex freigeben
                        new Ldi(42),
                        new Return(1)// Thread beenden
                }
        );
        assertEquals(86, interpreter.execute());
    }

    @Test
    public void testRaceCondition0() {
        /*Situation, als habe man Join vergessen, dann wird Halt() ausgefuert und 3 zurueckgegeben
        * Also der Thread wird nicht ausgefuert
        * */
        Interpreter interpreter = new Interpreter(
                new Instruction[] {
                        new Ldi(4),
                        new Fork(0),
                        new Ldi(3), //auf dieser Stelle wird nicht gejoint
                        new Halt(),       //weniger als 5 Intsruktionen -> Halt ohne Thread
                        new Ldi(5),
                        new Ldi(4),
                        new Ldi(13),
                        new Fork(0),
                        new Join(),
                        new Add(),
                        new Add(),
                        new Return(0),
                        new Ldi(-1),
                        new Ldi(3),
                        new Add(),
                        new Return(0)
                }
        );

        try {
            interpreter.execute();
            fail();
        } catch (SomeThreadNotClosed e) {
            assertEquals(0,0);
        }

    }

    @Test
    public void testRaceCondition01() {
        /*Situation, als habe man Join vergessen, dann wird Halt() ausgefuert und 3 zurueckgegeben
         * Also der Thread wird nicht ausgefuert
         * Jetzt aber mit mehr als 5 Intsruktionen. Es wird sowieso nicht gewartet und MainThread Terminiert füher
         * */
        Interpreter interpreter = new Interpreter(
                new Instruction[] {
                        new Ldi(8),
                        new Fork(0),
                        new Ldi(-1),
                        new Ldi(-1),
                        new Ldi(-1),
                        new Add(),
                        new Add(),
                        new Halt(),       //auf dieser Stelle wird nicht gejoint
                        new Ldi(5), //8
                        new Ldi(4),
                        new Ldi(16),
                        new Fork(0),
                        new Join(),
                        new Add(),
                        new Add(),
                        new Return(0),
                        new Ldi(-1),
                        new Ldi(3),
                        new Add(),
                        new Return(0)
                }
        );


        try {
            interpreter.execute();
            fail();
        } catch (SomeThreadNotClosed e) {
            assertEquals(0,0);
        }
    }

    @Test
    public void nochTest() {
        Interpreter interpreter = new Interpreter(new Instruction[]{
                new Ldi(1),
                new Alloc(),
                new Pop(0),
                new Push(0),
                new Ldi(21),
                new Fork(1),
                new Push(0),
                new Lock(),
                new Ldi(1),
                new Push(0),
                new Sth(),
                new Nop(),
                new Nop(),
                new Nop(),
                new Push(0),
                new Unlock(),
                new Join(),
                new Pop(0),
                new Push(0),
                new Lfh(),
                new Halt(),

                new Lfs(0),
                new Lock(),
                new Lfs(0),
                new Lfh(),
                new Ldi(10),
                new Mul(),
                new Ldi(5),
                new Add(),
                new Lfs(0),
                new Sth(),
                new Lfs(0),  //neu
                new Unlock(),//neu
                new Ldi(0),
                new Return(1)
        });
        assertEquals(15, interpreter.execute());
    }
}
