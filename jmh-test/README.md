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
ArrayVsByteStack.pushAndPop         LIST    1000  avgt   10  0,004 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop         LIST   10000  avgt   10  0,036 ±  0,007  ms/op
ArrayVsByteStack.pushAndPop         LIST  100000  avgt   10  0,301 ±  0,096  ms/op
ArrayVsByteStack.pushAndPop        STACK    1000  avgt   10  0,002 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop        STACK   10000  avgt   10  0,050 ±  0,093  ms/op
ArrayVsByteStack.pushAndPop        STACK  100000  avgt   10  1,184 ±  1,073  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK    1000  avgt   10  0,003 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK   10000  avgt   10  0,026 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK  100000  avgt   10  0,228 ±  0,029  ms/op
```