package asm;

import Exceptions.*;
import util.Terminal;

import java.util.*;

/**
 * Alle Exceptions sind im eigenen Package
 *  • Die ID des Threads, auf dessen Beendigung durch JOIN gewartet werden soll, existiert nicht. == InvalidThreadId()
 *      (tritt sowohl in Join, als auch in threadChange auf)
 *
 *  • Ein Lock wird freigegeben, obwohl es gar nicht blockiert ist. == AddressWasNeverLockedException()
 *  • Kein Thread ist lauffähig, es liegt also Deadlock vor. == DeadlockException() (triit in threadChange() auf)
 *  • Am Ende des Programms gibt es noch weitere Threads als den Main-Thread. == SomeThreadNotClosed()
 *  • Am Ende des Programms sind noch Locks blockiert. == LocksWereNotReleasedException()
 *  • Die Instruktion HALT wird außerhalb des Main-Threads verwendet. == InvalidHaltPlaceException
 * */
public class Interpreter implements AsmVisitor {
  private int[] heap = new int[1024];
  private int heapTop = 0;
  private Instruction[] program;
  private boolean halted;

  private int IDCounter = 0;
  private InterpreterThread currentThread;
  private ArrayList<InterpreterThread> threads = new ArrayList<>();

  private ArrayDeque<InterpreterThread> ready = new ArrayDeque<>();
  private HashSet<Integer> running = new HashSet<>();

  //Key - HeapAdresse, Values - alle Threads die auf diesen Lock warten
  private HashMap<Integer, HashSet<InterpreterThread>> blocked = new HashMap<>();
  private HashMap<Integer, InterpreterThread> address_Thread = new HashMap<>();

  //Key - der Thread, auf dem alle aus HashSet warten
  //HashSet - alle auf dem Key wartende Threads //ohne Gewaehr
  //joining enthält alle threads
  private HashMap<InterpreterThread, HashSet<InterpreterThread>> joining = new HashMap<>();

  public static int true_() {
    return -1;
  }

  public static int false_() {
    return 0;
  }

  public Interpreter(Instruction[] program) {
    this.program = program;
  }

  public int execute() {
    InterpreterThread mainThread = new InterpreterThread(0);
    threads.add(mainThread);
    joining.put(mainThread, new HashSet<>());
    running.add(0);

    IDCounter++;

    currentThread = mainThread;

    while (!halted) {
      if(currentThread.getkCounter() == 5) {
        currentThread.setkCounter(0);
        threadChange();
      }

      Instruction next = program[currentThread.getProgramCounter()];
      currentThread.incProgramCounter();
      next.accept(this);
    }

    return currentThread.pop();
  }

  //zuerst kriegt der nächste ID aus ready-Threads, dann
  // currentThread zum ready wechseln. Und currentTrhead = neue Thread
  private void threadChange() {
    /**Wenn dieser Thread schon fertig ist und es liegt keinen neuen ready-Thread vor*/
    if(currentThread.fertig && ready.size() == 0)
      throw new DeadlockException();
    if(ready.size() == 0)
      ready.add(currentThread);
    int newCurrentThreadID = ready.iterator().next().getId();

    threads.forEach(thread -> {
      //wenn der thread mit so einem ID gefunden ist, dann ist der neue currentThread
      if(thread.getId() == newCurrentThreadID) {
        if(!ready.contains(currentThread))
          ready.add(currentThread); //der alte Thread sollte jetzt zu ready kommen
        currentThread = thread;

        ready.remove(currentThread);
      }
    });
  }

  //wird in threads nach ID gesuncht
  //nützlich für Threads, die nicht in ready sind
  private void threadChange(int ID) {
    if(ID < 0)
      throw new InvalidThreadId();

    final boolean[] noId = {true};

    threads.forEach(t -> {
      if(t.getId() == ID) {
        currentThread = t;

        ready.remove(currentThread);
        noId[0] = false;
      }
    });

    if(noId[0])
      throw new InvalidThreadId();

    if(currentThread.fertig)
      throw new DeadlockException();
  }


  @Override
  public void visit(Fork fork) {
    InterpreterThread thread = new InterpreterThread(IDCounter);
    joining.put(thread, new HashSet<>());

    ready.add(thread);
    threads.add(thread);

    thread.push(-1);
    thread.push(-1);

    if (fork.getAddress() < 0)
      throw new InvalidNumberOfForkParametersException();
    int functionAddress = currentThread.pop();
    if (functionAddress < 0 || functionAddress > program.length)
      throw new InvalidForkAddressException();
    int[] arguments = new int[fork.getAddress()];
    for (int i = 0; i < arguments.length; i++)
      arguments[i] = currentThread.pop();

    for (int i = 0; i < arguments.length; i++)
      thread.push(arguments[arguments.length - 1 - i]);
    thread.setFramePointer(thread.getStackPointer());
    thread.setProgramCounter(functionAddress);

    currentThread.push(IDCounter); //ID vom neuen Thread hinterlassen
    IDCounter++;

    currentThread.inckCounter();
    //declAllowed = true;
    currentThread.setDeclAllowed(true);
  }

  @Override
  public void visit(Join join) {
    int ID = currentThread.pop();
    if(ID == 0) {
      throw new InvalidJoinTarget();
    } else if(ID < 0) {
      throw new InvalidThreadId();
    }

    threads.forEach(thread -> {
      if(thread.getId() == ID && thread.fertig == false) {
        joining.get(thread).add(currentThread);

        threadChange(ID); //es muss mit diesem ID-Thread weitergemacht werden, nicht mit dem current
        ready.remove(thread);
      } else if(thread.getId() == ID && thread.fertig == true) {
        //falls der Thread früher schon terminiert hat
        currentThread.push(thread.getReturnValueByReturn());
      }
    });
  }

  @Override
  public void visit(Lock lock) {
    int heapAddress = currentThread.pop();

    if(heapAddress < 0)
      throw new InvalidLockHeapAddressException();
    if(!blocked.containsKey(heapAddress))
      throw new NoSuchHeapAddressException();

    if(!address_Thread.containsKey(heapAddress))
      address_Thread.put(heapAddress, currentThread);
    else {
      currentThread.setProgramCounter(currentThread.getProgramCounter()-2); //wiederholt versuchen zu locken
      blocked.get(heapAddress).add(currentThread);
      ready.remove(currentThread);
      threadChange(address_Thread.get(heapAddress).getId()); //weiter mit dem Thread machen, der blockiert
      //oder einfach: threadChange();
    }

  }

  @Override
  public void visit(Unlock unlock) {
    int heapAddress = currentThread.pop(); //soll freigegeben werden

    if(!address_Thread.containsKey(heapAddress)) {
      throw new AddressWasNeverLockedException();
    } else if(address_Thread.containsKey(heapAddress) && blocked.get(heapAddress).isEmpty()) {
      //dann versucht der Thread sein igenes Lock freizugeben
      address_Thread.remove(heapAddress);
    } else if(address_Thread.containsKey(heapAddress) && !blocked.get(heapAddress).isEmpty()){
      //ready.add(blocked.get(heapAddress).iterator().next());
      ready.addAll(blocked.get(heapAddress));

      blocked.get(heapAddress).clear();

      address_Thread.remove(heapAddress);
    }
  }

  /**Return - eine Art Unjoin für Join(analog zu  Unlock). Alledings wird der Thread nicht zu
   * ready hinzugefügt, sondern sofort ausgeführt
   * */
  @Override
  public void visit(Return ret) {
    if (ret.getCells() < 0)
      throw new InvalidStackFrameSizeException();
    int retVal = currentThread.pop();
    for (int i = 0; i < ret.getCells(); i++)
      currentThread.pop();

    int retAdresse = currentThread.pop();
    if(retAdresse == -1) {
      currentThread.fertig = true;
      //dann weiss ich, dass der Thread beendet wurde und es muss dann derjenige dran, der auf diesen
      // Thread gewartet hat
      currentThread.setProgramCounter(retAdresse);
      currentThread.setFramePointer(currentThread.pop());
      currentThread.setStackPointer(retAdresse);

      currentThread.setReturnValueByReturn(retVal); //weil der Thread früher terminieren kann, als gejoint wird

      //joining Freigeben, wird immer ausgefuehrt, weil in joining alle Threads vorliegen die warten
      joining.get(currentThread).forEach(thread -> {
        thread.push(retVal);
        ready.add(thread);
      });

      InterpreterThread threadToRemove = currentThread;
      threadChange();
      ready.remove(threadToRemove);

    } else {
      currentThread.setProgramCounter(retAdresse);
      currentThread.setFramePointer(currentThread.pop());
      currentThread.push(retVal);
    }

    //declAllowed = false;
    currentThread.setDeclAllowed(false);
  }


  @Override
  public void visit(Add add) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a + b);

    currentThread.inckCounter();
    currentThread.setDeclAllowed(false);
  }

  @Override
  public void visit(Decl decl) {
    if (!currentThread.isDeclAllowed())
      throw new InvalidDeclarationException();
    if (decl.getCount() < 0)
      throw new InvalidStackAllocationException();
    currentThread.setStackPointer(currentThread.getStackPointer() + decl.getCount());

    currentThread.inckCounter();
  }

  @Override
  public void visit(And and) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a & b);

    currentThread.inckCounter();
    currentThread.setDeclAllowed(false);
  }

  @Override
  public void visit(Brc brc) {
    if (brc.getTarget() < 0 || brc.getTarget() > program.length)
      throw new InvalidJumpTargetException();
    int cond = currentThread.pop();
    if (cond == -1)
      currentThread.setProgramCounter(brc.getTarget());
      //programCounter = brc.getTarget();


    currentThread.inckCounter();
    currentThread.setDeclAllowed(false);
  }

  @Override
  public void visit(Call call) {
    if (call.getArgCount() < 0)
      throw new InvalidNumberOfMethodParametersException();
    int functionAddress = currentThread.pop();
    if (functionAddress < 0 || functionAddress > program.length)
      throw new InvalidMethodAddressException();
    int[] arguments = new int[call.getArgCount()];
    for (int i = 0; i < arguments.length; i++)
      arguments[i] = currentThread.pop();
    currentThread.push(currentThread.getFramePointer());
    currentThread.push(currentThread.getProgramCounter());
    for (int i = 0; i < arguments.length; i++)
      currentThread.push(arguments[arguments.length - 1 - i]);
    currentThread.setFramePointer(currentThread.getStackPointer());
    currentThread.setProgramCounter(functionAddress);

    currentThread.inckCounter();
    currentThread.setDeclAllowed(true);
  }

  @Override
  public void visit(Cmp cmp) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    switch (cmp.getCompareType()) {
      case EQ:
        currentThread.push(a == b ? true_() : false_());
        break;
      case LT:
        currentThread.push(a < b ? true_() : false_());
    }

    currentThread.inckCounter();
    currentThread.setDeclAllowed(false);
  }

  @Override
  public void visit(Div div) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a / b);

    currentThread.inckCounter();
    currentThread.setDeclAllowed(false);
  }

  @Override
  public void visit(Halt halt) {
    if(currentThread.getId() != 0)
      throw new InvalidHaltPlaceException();
    else {
      if(!address_Thread.isEmpty())
        throw new LocksWereNotReleasedException();

      halted = true;
      currentThread.inckCounter();
    }
    threads.forEach(thread -> {
      if(thread.getId() != 0 && !thread.fertig)
        throw new SomeThreadNotClosed();
    });
  }

  @Override
  public void visit(Ldi ldi) {
    currentThread.push(ldi.getValue());
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Lfs lds) {
    int stackAddress = currentThread.getFramePointer() + lds.getIndex();
    if (stackAddress < 0 || stackAddress >= currentThread.getStack().length)
      throw new InvalidStackAccessException();
    currentThread.push(currentThread.getStack()[stackAddress]);
    currentThread.setDeclAllowed(false);

    currentThread.inckCounter();
  }

  @Override
  public void visit(Mod mod) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a % b);
    currentThread.setDeclAllowed(false);

    currentThread.inckCounter();
  }

  @Override
  public void visit(Mul mul) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a * b);
    currentThread.setDeclAllowed(false);

    currentThread.inckCounter();
  }

  @Override
  public void visit(Nop nop) {
    currentThread.setDeclAllowed(false);

    currentThread.inckCounter();
  }

  @Override
  public void visit(Not not) {
    int a = currentThread.pop();
    currentThread.push(~a);
    currentThread.setDeclAllowed(false);

    currentThread.inckCounter();
  }

  @Override
  public void visit(Or or) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a | b);
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Pop pop) {
    int a = currentThread.pop();
    currentThread.getRegisters()[pop.getRegister()] = a;
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Push push) {
    currentThread.push(currentThread.getRegisters()[push.getRegister()]);
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(In read) {
    int value = Terminal.askInt("Zahl eingeben: ");
    currentThread.push(value);
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Sts sts) {
    int stackAddress = currentThread.getFramePointer() + sts.getIndex();
    if (stackAddress < 0 || stackAddress >= currentThread.getStack().length)
      throw new InvalidStackAccessException();
    int value = currentThread.pop();
    currentThread.getStack()[stackAddress] = value;
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Sub sub) {
    int a = currentThread.pop();
    int b = currentThread.pop();
    currentThread.push(a - b);
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Out write) {
    int value = currentThread.pop();
    System.out.println(value);
    currentThread.setDeclAllowed(false);
    currentThread.inckCounter();
  }

  @Override
  public void visit(Lfh lfh) {
    int hAddr = currentThread.pop();
    if (hAddr < 0 || hAddr >= heap.length)
      throw new HeapAccessException();

    currentThread.push(heap[hAddr]);
    currentThread.inckCounter();

  }

  @Override
  public void visit(Sth sth) {
    int hAddr = currentThread.pop();
    if (hAddr < 0 || hAddr >= heap.length)
      throw new HeapAccessException();


    int value = currentThread.pop();
    heap[hAddr] = value;
    currentThread.inckCounter();
  }

  @Override
  public void visit(Alloc alloc) {
    int size = currentThread.pop();
    if (size == 0)
      size++;
    if (size < 0 || heapTop + size > heap.length)
      throw new HeapAllocationException();
    currentThread.push(heapTop);

    blocked.put(heapTop, new HashSet<>()); //ich erstelle für jede Adresse eine Menge der gelockten Threads

    heapTop += size;
    currentThread.inckCounter();
  }
}
