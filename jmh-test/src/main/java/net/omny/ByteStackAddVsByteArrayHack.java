package net.omny;

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
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import net.omny.utils.ByteStack;

@State(Scope.Benchmark)
public class ByteStackAddVsByteArrayHack {

    @Param({ "byte", "array" })
    private String type;
    @Param({ "1000", "10000", "100000" })
    public int size;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayVsByteStack.class.getSimpleName())

                .shouldDoGC(true)
                .resultFormat(ResultFormatType.TEXT)
                .result("benchmark-result/BStack2_" + System.currentTimeMillis() + ".txt")
                .forks(1).build();

        new Runner(opt).run();
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 5, time = 2)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void pushAndPop() {
        pushAndPop(new ByteStack());
    }

    private void pushAndPop(ByteStack byteStack) {
        if (type.equals("byte")) {
            for (int i = 0; i < this.size; i++) {
                byteStack.push((byte) 69);
                byteStack.pop();
            }
        } else {
            for (int i = 0; i < this.size; i++) {
                byteStack.pushArrayHack((byte) 69);
                byteStack.pop();
            }
        }
    }

}
