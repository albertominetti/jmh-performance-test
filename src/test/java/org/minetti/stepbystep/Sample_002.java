package org.minetti.stepbystep;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Sample_002 {

    private static Random random = new Random();

    @Benchmark
    public double measure() {
        return Math.log(random.nextDouble());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Sample_002.class.getSimpleName())
                .forks(1)
                .warmupIterations(1).warmupTime(TimeValue.seconds(2))
                .measurementIterations(5).measurementTime(TimeValue.seconds(3))
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.MICROSECONDS)
                .shouldDoGC(false)
                .shouldFailOnError(true)
                // and more
                .build();

        new Runner(opt).run();
    }

    /*

# JMH version: 1.23
# VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
# VM invoker: C:\Program Files\Java\jdk1.8.0_221\jre\bin\java.exe
# VM options: -javaagent:C:\Apps\ideaIU-2019.2.4.win\lib\idea_rt.jar=57765:C:\Apps\ideaIU-2019.2.4.win\bin -Dfile.encoding=UTF-8
# Warmup: 1 iterations, 2 s each
# Measurement: 5 iterations, 3 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.csg.eap.beacon.validation.jmh.Sample_002.measure

# Run progress: 0,00% complete, ETA 00:01:08
# Fork: 1 of 1
# Warmup Iteration   1: 17,249 ops/us
Iteration   1: 18,168 ops/us
Iteration   2: 18,383 ops/us
Iteration   3: 18,315 ops/us
Iteration   4: 18,408 ops/us
Iteration   5: 18,631 ops/us


Result "com.csg.eap.beacon.validation.jmh.Sample_002.measure":
  18,381 ±(99.9%) 0,647 ops/us [Average]
  (min, avg, max) = (18,168, 18,381, 18,631), stdev = 0,168
  CI (99.9%): [17,734, 19,028] (assumes normal distribution)


 */


    @Benchmark
    public void measureWrong() {
        Math.log(random.nextDouble());
    }


    @Benchmark
    @OperationsPerInvocation(100)
    public void measureMultipleWrong() {
        for (int i = 0; i < 100; i++) {
            Math.log(random.nextDouble());
        }
    }


    @Benchmark
    @OperationsPerInvocation(100)
    public void measureMultiple(Blackhole bh) {
        for (int i = 0; i < 100; i++) {
            bh.consume(Math.log(random.nextDouble()));
        }
    }


    /*

Benchmark                         Mode  Cnt   Score    Error   Units
Sample_002.measure               thrpt    5  18,381 ±  0,647  ops/us
Sample_002.measureMultiple       thrpt    5  18,745 ±  0,501  ops/us
Sample_002.measureMultipleWrong  thrpt    5  37,021 ± 12,960  ops/us
Sample_002.measureWrong          thrpt    5  35,841 ±  7,644  ops/us

     */
}