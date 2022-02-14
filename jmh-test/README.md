# Omny JMH Test 
## Testing implementation using OpenJDK Java Microbenchmark Harness

#### Comparing ArrayList, Stack (from the JDK17) and ByteStack (my own datastruct, here named NATIVESTACK)

Operations tested:
- pushing a byte (add it to the list)
- pushing an array of byte (merging 2 array)

This datastructure is used in the framework in order to write into the response buffer.
it act as a resizable byte buffer.

#### Pushing a bytes:
This operation consist of two method call, repeated `size` times.
```java
  list.add((byte) 69); // pushing
  list.remove(0);      // poping
```
Here recaps:
```
Benchmark                     (implType)  (size)  Mode  Cnt   Score    Error  Units
ArrayVsByteStack.addAll             LIST    1000  avgt    5   0,003 ±  0,001  ms/op
ArrayVsByteStack.addAll             LIST   10000  avgt    5   0,034 ±  0,007  ms/op
ArrayVsByteStack.addAll             LIST  100000  avgt    5   0,274 ±  0,025  ms/op
ArrayVsByteStack.addAll            STACK    1000  avgt    5   0,010 ±  0,001  ms/op
ArrayVsByteStack.addAll            STACK   10000  avgt    5   0,091 ±  0,012  ms/op
ArrayVsByteStack.addAll            STACK  100000  avgt    5   0,957 ±  0,136  ms/op
ArrayVsByteStack.addAll      NATIVESTACK    1000  avgt    5   0,001 ±  0,001  ms/op
ArrayVsByteStack.addAll      NATIVESTACK   10000  avgt    5   0,020 ±  0,015  ms/op
ArrayVsByteStack.addAll      NATIVESTACK  100000  avgt    5   0,103 ±  0,032  ms/op
```
#### Pushing an array of byte
This operation is a lot of calls:
- init the List/Stack/ByteStack with `size/2` elements.
- create a `List<Byte> / byte[]` 

```java
  list.addAll({69, 69, 69, .... 69}); // pushing array
```
Here recaps:
```
Benchmark                     (implType)  (size)  Mode  Cnt   Score    Error  Units
ArrayVsByteStack.pushAndPop         LIST    1000  avgt    5   0,004 ±  0,003  ms/op
ArrayVsByteStack.pushAndPop         LIST   10000  avgt    5   0,040 ±  0,035  ms/op
ArrayVsByteStack.pushAndPop         LIST  100000  avgt    5   0,228 ±  0,008  ms/op
ArrayVsByteStack.pushAndPop        STACK    1000  avgt    5   0,002 ±  0,001  ms/op
ArrayVsByteStack.pushAndPop        STACK   10000  avgt    5   0,128 ±  0,250  ms/op
ArrayVsByteStack.pushAndPop        STACK  100000  avgt    5   1,100 ±  3,134  ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK    1000  avgt    5  ? 10??           ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK   10000  avgt    5  ? 10??           ms/op
ArrayVsByteStack.pushAndPop  NATIVESTACK  100000  avgt    5  ? 10??           ms/op
```