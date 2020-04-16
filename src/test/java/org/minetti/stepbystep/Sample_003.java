package org.minetti.stepbystep;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
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
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class Sample_003 {

    @State(Scope.Benchmark)
    public static class RandomHolder {
        Random random = new Random();
    }

    @Benchmark
    public double measure(RandomHolder randomHolder) {
        return Math.log(randomHolder.random.nextDouble());
    }

    @Benchmark
    @OperationsPerInvocation(100)
    public void measureMultiple(RandomHolder randomHolder, Blackhole bh) {
        for (int i = 0; i < 100; i++) {
            bh.consume(Math.log(randomHolder.random.nextDouble()));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Sample_003.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }


    /*
Benchmark                           Mode  Cnt   Score    Error  Units
Sample_003.measure                  avgt    3  53,713 ±  4,166  ns/op
Sample_003.measureMultiple          avgt    3  54,713 ± 35,304  ns/op
    */





    @State(Scope.Benchmark)
    public static class ListHolder {
        List<Double> list = new ArrayList<>();

        @Setup(Level.Trial)
        public void setUp(RandomHolder randomHolder) {
            IntStream.rangeClosed(1, 100).forEach(i -> {
                list.add(randomHolder.random.nextDouble());
            });
        }
    }


    @Benchmark
    @OperationsPerInvocation(100)
    public void measureMultipleWithList(ListHolder listHolder, Blackhole bh) {
        listHolder.list.forEach(i -> {
            bh.consume(Math.log(i));
        });
    }

    /*
Benchmark                           Mode  Cnt   Score    Error  Units
Sample_003.measure                  avgt    3  53,713 ±  4,166  ns/op
Sample_003.measureMultiple          avgt    3  54,713 ± 35,304  ns/op
Sample_003.measureMultipleWithList  avgt    3  31,475 ± 34,777  ns/op
     */

}