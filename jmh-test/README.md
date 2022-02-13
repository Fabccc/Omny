# Omny JMH Test 
## Testing implementation using OpenJDK Java Microbenchmark Harness

#### Comparing ArrayList, Stack (from the JDK17) and ByteStack (my own datastruct, here named NATIVESTACK)

This datastructure is used in the framework in order to write into the response buffer.
it act as a resizable byte buffer.

This operation consist of two method call, repeated `size` times.
```java
  list.add((byte) 69); // pushing
  list.remove(0);      // poping
```
Here recaps:
```
Benchmark                     (implType)  (size)  Mode  Cnt  Score    Error  Units
ArrayVsByteStack.pushAndPop         LIST    1000  avgt   10  0,004 ±  0,002  ms/op
ArrayVsByteStack.pushAndPop         LIST   10000  avgt   10  0,043 ±  0,013  ms/op
ArrayVsByteStack.pushAndPop         LIST  100000  avgt   10  0,456 ±  0,116  ms/op
ArrayVsByteStack.pushAndPop        STACK    1000  avgt   10  0,002 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop        STACK   10000  avgt   10  0,020 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop        STACK  100000  avgt   10  1,775 ±  0,849  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK    1000  avgt   10  0,003 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK   10000  avgt   10  0,033 ±  0,007  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK  100000  avgt   10  0,333 ±  0,053  ms/op
```