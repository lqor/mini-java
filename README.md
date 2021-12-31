# Mini Java
Im Rahmen des Praktikums haben wir ein Interpreter zu userer Programmiersprache geschrieben.
Mini Java unterstützt:
* Schleifen 
* Methoden aufrufen
* Alle oft vorkommende Konstruktionen (if..else, if...else if..., case... usw)
* Variablen-Deklaration
* Arrays (mit variabler Größe)
* Threads (dazu unten mehr)
### Threads in MiniJava
In letzter Woche haben wir den Interpreter und die Code-Generierung um Threading erweitert, sodass Pseudo-Parallelität in MiniJava-Programmen verwendet werden kann. 

Wir haben auf der Seite der VM allerdings nur einen einzigen Java-Thread verwenden. Die MiniJava-Programme laufen daher nicht wirklich parallel – ähnlich zu Java-Programmen, die auf einem Computer ausgeführt werden, der lediglich über einen einzigen Core verfügt. 

Stattdessen entscheidet user Scheduler, von welchem Thread als nächstes ein Satz Instruktionen ausgeführt wird.
