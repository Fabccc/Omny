/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.omny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import net.omny.utils.ByteStack;
import net.omny.utils.Primitive;

@State(Scope.Benchmark)
public class ArrayVsByteStack {

    @Param({ "LIST", "STACK", "NATIVESTACK" })
    public String implType;

    @Param({ "1000", "10000", "100000" })
    public int size;

    private byte[] arr;
    private List<Byte> arr2;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayVsByteStack.class.getSimpleName())
                
            .shouldDoGC(true)
            .resultFormat(ResultFormatType.TEXT)
            .result("benchmark-result/BStack_" + System.currentTimeMillis() + ".txt")
                .forks(1).build();

        new Runner(opt).run();
    }

    public List<Byte> createBytes() {
        if (this.implType.equals("LIST")) {
            return new ArrayList<>();
        } else if (this.implType.equals("STACK")) {
            return new Stack<>();
        } else {
            return new ByteStack();
        }
    }

    @Setup(Level.Iteration)
    public void setup(){
        int initialLength = this.size / 2;
        byte[] toPush = new byte[initialLength];
        Arrays.fill(toPush, (byte) 35);
        this.arr = toPush;
        this.arr2 = Primitive.toList(toPush);

    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 5, time = 2)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void pushAndPop() {
        pushAndPop(createBytes());
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1, time = 2)
    @Measurement(iterations = 5, time = 2)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void addAll() {
        addAll(createBytes());
    }

    private void push(List<Byte> bytes) {
        /*
         * The problem with java is that it wrap this byte in an Object primitive
         * wrapper (java.lang.Byte).
         * as List is an interface, my implementation need to declare the add method
         * with the Object wrapper.
         * but my implementation has a method that accept a primitive byte type.
         * This is why i'm using a instance type checking
         */
        if (bytes instanceof ByteStack stack) {
            stack.push((byte) 69);
        } else {
            bytes.add((byte) 69);
        }
    }

    private void addAll(List<Byte> bytes) {
        // Fill the initial array 
        for(int i = 0; i < size / 2-1; i++){
            push(bytes);
        }
        // Merge
        merge(bytes);
    }

    private void merge(List<Byte> bytes){
        if (bytes instanceof ByteStack stack) {
            stack.push(this.arr);
        } else {
            bytes.addAll(this.arr2);
        }
    }

    private void pushAndPop(List<Byte> bytes) {
        for (int i = 0; i < this.size; i++) {
            push(bytes);
            bytes.remove(0);
        }
    }

}
