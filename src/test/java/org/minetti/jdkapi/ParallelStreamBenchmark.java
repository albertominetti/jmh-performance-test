package org.minetti.jdkapi;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(1)
public class ParallelStreamBenchmark {

    public static final int SIZE = 10_000_000;

    public static final Supplier<Long> randomGen = () -> ThreadLocalRandom.current().nextLong();

    @Benchmark
    public List<Long> usingOldStyleFor() {
        List<Long> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            list.add(randomGen.get());
        }
        return list;
    }

    @Benchmark
    public List<Long> usingStream() {
        return Stream.generate(randomGen).limit(SIZE).collect(toList());
    }

    @Benchmark
    public List<Long> usingParallelStream() {
        return Stream.generate(randomGen).parallel().limit(SIZE).collect(toList());
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ParallelStreamBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    /*
(higher is better)
Benchmark                                        Mode  Cnt  Score   Error  Units
ParallelStreamBenchmark.usingOldStyleFor        thrpt    3  6,709 ± 2,483  ops/s
ParallelStreamBenchmark.usingStream             thrpt    3  4,141 ± 0,983  ops/s
ParallelStreamBenchmark.usingParallelStream     thrpt    3  1,563 ± 9,338  ops/s
     */

}
