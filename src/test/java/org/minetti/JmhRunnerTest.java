package org.minetti;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Disabled
public class JmhRunnerTest {

    @Test
    public void launchAllBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                .include(this.getClass().getPackageName() + ".*")
                .build();

        new Runner(opt).run();
    }
}
