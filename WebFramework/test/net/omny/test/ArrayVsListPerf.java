package net.omny.test;

import java.util.ArrayList;
import java.util.List;

import net.omny.utils.ByteStack;

public class ArrayVsListPerf {
  
  public static void main(String[] args) {
    ByteStack bStack = new ByteStack();
    List<Byte> bList = new ArrayList<>();
    
    PerformanceTest.printRecap("ByteStack::add", () -> {
      for(int i = 0; i < 10_000_000; i++){
        bStack.add((byte) (i%255));
      }
    });
    PerformanceTest.printRecap("ArrayList::add", () -> {
      for(int i = 0; i < 10_000_000; i++){
        bList.add((byte) (i%255));
      }
    });
  }

}
