package org.minetti.jdkapi;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Fork(1)
@State(Scope.Benchmark)
public class UnorderedStreamBenchmark {

    public static final int SIZE = 1_000_000;
    private static List<Integer> ints;

    @Setup(Level.Trial)
    public static void init() {
        ints = IntStream.rangeClosed(1, SIZE).boxed().collect(toList());
        Collections.shuffle(ints);
    }

    @Benchmark
    public Set<Integer> intoSet() {
        return ints.stream().parallel().collect(toSet());
    }

    @Benchmark
    public Set<Integer> unorderedIntoSet() {
        return ints.stream().unordered().parallel().collect(toSet());
    }

    @Benchmark
    public Set<Integer> distinctIntoSet() {
        return ints.stream().parallel().distinct().collect(toSet());
    }

    @Benchmark
    public Set<Integer> unorderedDistinctIntoSet() {
        return ints.stream().unordered().parallel().distinct().collect(toSet());
    }

    @Benchmark
    public List<Integer> distinctIntoList() {
        return ints.stream().parallel().distinct().collect(toList());
    }

    @Benchmark
    public List<Integer> unorderedDistinctIntoList() {
        return ints.stream().unordered().parallel().distinct().collect(toList());
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(UnorderedStreamBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .build();
        new Runner(opt).run();
    }

    /*
(higher is better)
Benchmark                                            Mode  Cnt   Score    Error  Units
UnorderedStreamBenchmark.intoSet                    thrpt    3   6,737 ±  1,583  ops/s
UnorderedStreamBenchmark.unorderedIntoSet           thrpt    3   6,624 ±  0,383  ops/s
UnorderedStreamBenchmark.distinctIntoSet            thrpt    3   0,268 ±  0,317  ops/s
UnorderedStreamBenchmark.unorderedDistinctIntoSet   thrpt    3   4,636 ±  1,818  ops/s
UnorderedStreamBenchmark.distinctIntoList           thrpt    3   3,843 ±  0,758  ops/s
UnorderedStreamBenchmark.unorderedDistinctIntoList  thrpt    3  27,283 ± 10,594  ops/s
     */
}
