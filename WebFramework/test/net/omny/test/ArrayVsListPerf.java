package net.omny.test;


import java.util.ArrayList;
import java.util.List;

import net.omny.utils.ByteStack;
import net.omny.utils.PerformanceTest;

public class ArrayVsListPerf {
  
  public static void main(String[] args) {
    ByteStack bStack = new ByteStack();
    List<Byte> bList = new ArrayList<>();
    
    for(int i = 0; i < 255; i++) {
      bStack.add((byte) i);
      bList.add((byte) i);
    }
    bStack.clear();
    bList.clear();


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
