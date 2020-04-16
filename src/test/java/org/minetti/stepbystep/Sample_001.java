package org.minetti.stepbystep;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class Sample_001 {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAvgTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Sample_001.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    /*

# JMH version: 1.23
# VM version: JDK 1.8.0_221, Java HotSpot(TM) 64-Bit Server VM, 25.221-b11
# VM invoker: C:\Program Files\Java\jdk1.8.0_221\jre\bin\java.exe
# VM options: -javaagent:C:\Apps\ideaIU-2019.2.4.win\lib\idea_rt.jar=51839:C:\Apps\ideaIU-2019.2.4.win\bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.csg.eap.beacon.validation.jmh.Sample_001.measureAvgTime

# Run progress: 50,00% complete, ETA 00:06:53
# Fork: 1 of 1
# Warmup Iteration   1: 1000414,800 ns/op
# Warmup Iteration   2: 1000518,000 ns/op
# Warmup Iteration   3: 1000344,500 ns/op
# Warmup Iteration   4: 1000745,900 ns/op
# Warmup Iteration   5: 1000436,900 ns/op
Iteration   1: 1000371,100 ns/op
Iteration   2: 1000372,800 ns/op
Iteration   3: 1000475,100 ns/op
Iteration   4: 1000650,500 ns/op
Iteration   5: 1000379,100 ns/op


Result "com.csg.eap.beacon.validation.jmh.Sample_001.measureAvgTime":
1000449796,920 ±(99.9%) 463537,039 ns/op [Average]
(min, avg, max) = (1000371364,100, 1000449796,920, 1000650379,500), stdev = 120379,097
CI (99.9%): [999986259,881, 1000913333,959] (assumes normal distribution)

     */



    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureMultiple() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAll() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    /*

Benchmark                                             Mode  Cnt           Score        Error   Units
Sample_001.measureAll                                thrpt    5          ≈ 10⁻⁶               ops/us
Sample_001.measureMultiple                           thrpt    5          ≈ 10⁻⁶               ops/us
Sample_001.measureThroughput                         thrpt    5           1,000 ±      0,001   ops/s
Sample_001.measureAll                                 avgt    5     1000485,785 ±    487,156   us/op
Sample_001.measureAvgTime                             avgt    5     1000485,920 ±    463,039   ns/op
Sample_001.measureMultiple                            avgt    5     1000313,001 ±    512,826   us/op
Sample_001.measureAll                               sample   50      999838,188 ±    261,967   us/op
Sample_001.measureAll:measureAll·p0.00              sample           999292,928                us/op
Sample_001.measureAll:measureAll·p0.50              sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p0.90              sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p0.95              sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p0.99              sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p0.999             sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p0.9999            sample          1000341,504                us/op
Sample_001.measureAll:measureAll·p1.00              sample          1000341,504                us/op
Sample_001.measureMultiple                          sample   50      999817,216 ±    282,373   us/op
Sample_001.measureMultiple:measureMultiple·p0.00    sample           998244,352                us/op
Sample_001.measureMultiple:measureMultiple·p0.50    sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p0.90    sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p0.95    sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p0.99    sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p0.999   sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p0.9999  sample          1000341,504                us/op
Sample_001.measureMultiple:measureMultiple·p1.00    sample          1000341,504                us/op
Sample_001.measureAll                                   ss          1000113,641                us/op
Sample_001.measureMultiple                              ss          1000721,641                us/op

     */


}