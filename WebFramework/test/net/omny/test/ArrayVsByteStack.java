package net.omny.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import net.omny.utils.ByteStack;

@State(Scope.Benchmark)
public class ArrayVsByteStack {
  
  @Param({"LIST", "STACK", "NATIVESTACK"})
  public String implType;

  @Param({"1000", "10000", "100000", "1000000"})
  public int size;

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
      .include(ArrayVsByteStack.class.getSimpleName())
      .forks(1).build();

      new Runner(opt).run();
  }

  public List<Byte> createBytes(){
    if(this.implType.equals("LIST")){
      return new ArrayList<>();
    }else if(this.implType.equals("STACK")){
      return new Stack<>();
    }else{
      return new ByteStack();
    }
  }

  @Benchmark
  @Fork(1)
  @Warmup(iterations = 5)
  @Measurement(iterations = 10)
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void pushAndPop(){
    var list = createBytes();
    pushAndPop(list);
  }

  private void pushAndPop(List<Byte> bytes){
    for(int i = 0; i < this.size; i++){
      bytes.add((byte) 69);
      bytes.remove(1);
    }
  }



}
