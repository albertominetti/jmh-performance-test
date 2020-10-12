package org.minetti.jdkapi;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.minetti.jdkapi.MultipleComparisonBenchmarks.TimeInForceHolder.N;
import static org.minetti.jdkapi.TimeInForce.*;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 2, time = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class MultipleComparisonBenchmarks {

    @State(Scope.Benchmark)
    public static class TimeInForceHolder {
        public final static int N = 250;
        private static final TimeInForce[] values = TimeInForce.values();
        public List<TimeInForce> data = new ArrayList<>();

        @Setup(Level.Trial)
        public void setup() {
            for (int i = 0; i < N; i++) {
                data.add(values[RandomUtils.nextInt(0, values.length)]);
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(N)
    public void compareUsingIfs(TimeInForceHolder timeInForceHolder, Blackhole blackhole) {
        for (TimeInForce t : timeInForceHolder.data) {
            blackhole.consume(usingIfs(t));
        }
    }

    @Benchmark
    @OperationsPerInvocation(N)
    public void compareUsingSwitch(TimeInForceHolder timeInForceHolder, Blackhole blackhole) {
        for (TimeInForce t : timeInForceHolder.data) {
            blackhole.consume(usingSwitch(t));
        }
    }

    @Benchmark
    @OperationsPerInvocation(N)
    public void compareUsingHashSet(TimeInForceHolder timeInForceHolder, Blackhole blackhole) {
        for (TimeInForce t : timeInForceHolder.data) {
            blackhole.consume(usingHashSet(t));
        }
    }

    @Benchmark
    @OperationsPerInvocation(N)
    public void compareUsingEnumSet(TimeInForceHolder timeInForceHolder, Blackhole blackhole) {
        for (TimeInForce t : timeInForceHolder.data) {
            blackhole.consume(usingEnumSet(t));
        }
    }


    private boolean usingIfs(TimeInForce timeInForce) {
        return DAY.equals(timeInForce) || FILL_OR_KILL.equals(timeInForce)
                || IMMEDIATE_OR_CANCEL.equals(timeInForce) || AT_THE_CLOSE.equals(timeInForce);
    }

    private boolean usingSwitch(TimeInForce timeInForce) {
        switch (timeInForce) {
            case DAY:
            case FILL_OR_KILL:
            case IMMEDIATE_OR_CANCEL:
            case AT_THE_CLOSE:
                return true;
            default:
                return false;
        }
    }

    private final static HashSet<TimeInForce> setTimeInForce = Sets.newHashSet(DAY, FILL_OR_KILL, IMMEDIATE_OR_CANCEL, AT_THE_CLOSE);

    private boolean usingHashSet(TimeInForce timeInForce) {
        return setTimeInForce.contains(timeInForce);
    }

    private final static EnumSet<TimeInForce> enumSetTimeInForce = Sets.newEnumSet(setTimeInForce, TimeInForce.class);

    private boolean usingEnumSet(TimeInForce timeInForce) {
        return enumSetTimeInForce.contains(timeInForce);
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(MultipleComparisonBenchmarks.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    /*

Benchmark                                           Mode  Cnt  Score   Error  Units
MultipleComparisonBenchmarks.compareUsingEnumSet  avgt    2  4,712          ns/op
MultipleComparisonBenchmarks.compareUsingHashSet  avgt    2  5,556          ns/op
MultipleComparisonBenchmarks.compareUsingIfs      avgt    2  4,894          ns/op
MultipleComparisonBenchmarks.compareUsingSwitch   avgt    2  4,451          ns/op

Benchmark (another run)                             Mode  Cnt  Score   Error  Units
MultipleComparisonBenchmarks.compareUsingEnumSet  avgt    2  5,676          ns/op
MultipleComparisonBenchmarks.compareUsingHashSet  avgt    2  6,146          ns/op
MultipleComparisonBenchmarks.compareUsingIfs      avgt    2  4,972          ns/op
MultipleComparisonBenchmarks.compareUsingSwitch   avgt    2  4,737          ns/op

     */
}


enum TimeInForce {
    DAY,
    GOOD_TILL_CANCEL,
    AT_THE_OPENING,
    IMMEDIATE_OR_CANCEL,
    FILL_OR_KILL,
    GOOD_TILL_CROSSING,
    GOOD_TILL_DATE,
    AT_THE_CLOSE
}

