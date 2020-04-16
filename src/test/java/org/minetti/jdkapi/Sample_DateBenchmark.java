package org.minetti.jdkapi;

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
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 2, time = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class Sample_DateBenchmark {

    @State(Scope.Benchmark)
    public static class DatesHolder {
        List<Date> dates = new ArrayList<>();

        @Setup(Level.Trial)
        public void setup() {
            long millisPerDay = 1000 * 60 * 60 * 24;
            long time = System.currentTimeMillis();
            Date now = new Date(time);
            dates.add(now);
            for (int i = 1; i <= 100; i++) {
                dates.add(new Date(time - i * millisPerDay));
            }
            for (int i = 1; i <= 100; i++) {
                dates.add(new Date(time + i * millisPerDay));
            }
        }
    }


    public boolean isBeforeUsingMillis(long systemTime, Date date) {
        return date.getTime() > systemTime;
    }

    public boolean isBeforeUsingCalendar(Calendar calendar, Date date) {
        return date.after(calendar.getTime());
    }

    public boolean isBeforeUsingJavaTime(Instant now, Date date) {
        return now.isBefore(date.toInstant());
    }


    @Benchmark
    @OperationsPerInvocation(201)
    public void instantiateNowAndCompareByCalendar(DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingCalendar(Calendar.getInstance(), date));
        }
    }

    @Benchmark
    @OperationsPerInvocation(201)
    public void instantiateNowAndCompareBySystemTime(DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingMillis(System.currentTimeMillis(), date));
        }
    }

    @Benchmark
    @OperationsPerInvocation(201)
    public void instantiateNowAndCompareByJavaTime(DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingJavaTime(Instant.now(), date));
        }
    }

    @State(Scope.Benchmark)
    public static class NowHolder {
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        Instant instant = Instant.now();
    }

    @Benchmark
    @OperationsPerInvocation(201)
    public void compareBySystemTime(NowHolder now, DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingMillis(now.systemTime, date));
        }
    }

    @Benchmark
    @OperationsPerInvocation(201)
    public void compareByJavaTime(NowHolder now, DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingJavaTime(now.instant, date));
        }
    }

    @Benchmark
    @OperationsPerInvocation(201)
    public void compareByCalendar(NowHolder now, DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            blackhole.consume(isBeforeUsingCalendar(now.calendar, date));
        }
    }


    @Benchmark
    @OperationsPerInvocation(201)
    public void compareByAllWrong(NowHolder now, DatesHolder datesHolder, Blackhole blackhole) {
        for (Date date : datesHolder.dates) {
            isBeforeUsingMillis(now.systemTime, date);
            isBeforeUsingJavaTime(now.instant, date);
            isBeforeUsingCalendar(now.calendar, date);
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(".*" + Sample_DateBenchmark.class.getSimpleName() + ".*compareByAll")
                .build();
        new Runner(opt).run();
    }

    /*
Benchmark                                                     Mode  Cnt    Score   Error  Units
DateConstraintBenchmark.compareByCalendar                     avgt   10    4,260 ± 0,027  ns/op
DateConstraintBenchmark.compareByJavaTime                     avgt   10    6,858 ± 0,037  ns/op
DateConstraintBenchmark.compareBySystemTime                   avgt   10    4,154 ± 0,008  ns/op
DateConstraintBenchmark.instantiateNowAndCompareByCalendar    avgt   10  132,140 ± 0,534  ns/op
DateConstraintBenchmark.instantiateNowAndCompareByJavaTime    avgt   10   11,160 ± 0,782  ns/op
DateConstraintBenchmark.instantiateNowAndCompareBySystemTime  avgt   10    6,975 ± 0,865  ns/op
     */
}
