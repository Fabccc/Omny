package net.omny.utils;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public final class PerformanceTest {
  
  private PerformanceTest(){}

  public static long performance(Runnable task){
    Stopwatch stopwatch = Stopwatch.createStarted();
    task.run();
    return stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
  }

  public static void printRecap(String name, Runnable task){
    long took = performance(task);
    System.out.println("Task "+name+" took "+took+" ms to run");
  }

}
