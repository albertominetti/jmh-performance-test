package org.minetti.stepbystep;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class Sample_004 {

    private Random random = new Random();

    @Param({"100", "1000", "10000"})
    private int size;

    @State(Scope.Benchmark)
    public static class ListHolder {
        List<Double> list = new ArrayList<>();

        @Setup(Level.Iteration)
        public void setUp(Sample_004 randomHolder) {
            IntStream.rangeClosed(1, randomHolder.size).forEach(i -> {
                list.add(randomHolder.random.nextDouble());
            });
        }
    }

    @Benchmark
    public void measureMultiple(ListHolder listHolder, Blackhole bh) {
        listHolder.list.forEach(i -> {
            bh.consume(Math.log(i));
        });
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Sample_004.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    /*
Benchmark                   (size)  Mode  Cnt    Score      Error  Units
Sample_004.measureMultiple     100  avgt    3    9,138 ±   51,129  us/op
Sample_004.measureMultiple    1000  avgt    3   91,939 ±  569,836  us/op
Sample_004.measureMultiple   10000  avgt    3  896,252 ± 5552,777  us/op
     */
}