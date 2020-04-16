package org.minetti.forex;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

public class ForexCalculatorBenchmarkTest {
    private static final double ONE_MILLISECOND = 1_000.0;
    // because the benchmark is annotated with @OutputTimeUnit(TimeUnit.MICROSECONDS)

    private static List<RunResult> sortedRunResults;
    private static List<RunResult> otherImplementationRunResults;

    @BeforeAll
    public static void launchBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ForexCalculatorBenchmark.class.getName() + "\\.")
                .build();

        Collection<RunResult> runResults = new Runner(opt).run();

        sortedRunResults = runResults.stream()
                .filter(r -> r.getPrimaryResult().getLabel().equals("onCurrentImplementation"))
                .sorted(comparing(ForexCalculatorBenchmarkTest::amountSize))
                .collect(toList());

        otherImplementationRunResults = runResults.stream()
                .filter(r -> r.getPrimaryResult().getLabel().equals("onAnotherImplementation"))
                .sorted(comparing(ForexCalculatorBenchmarkTest::amountSize))
                .collect(toList());
    }

    @Test
    public void belowTheReferenceTime() {

        assertThat(sortedRunResults, hasSize(3));
        for (RunResult runResult : sortedRunResults) {
            double avgTime = score(runResult);
            assertThat(avgTime, is(lessThan(ONE_MILLISECOND)));
        }
    }

    @Test
    public void complexityIsLinear() {

        for (int i = 1; i < sortedRunResults.size(); i++) {
            // checking if the spent time is linear based on the amountSize, so O(n)
            RunResult currentResult = sortedRunResults.get(i);
            RunResult previousResult = sortedRunResults.get(i - 1);
            double scoreRatio = score(currentResult) / score(previousResult);
            double sizeRatio = amountSize(currentResult) / amountSize(previousResult);
            assertThat(scoreRatio, is(not(greaterThan(sizeRatio * 1.2))));
        }
    }

    @Test
    public void isBetterThanTheOtherImplementation() throws Exception {

        assertThat(otherImplementationRunResults, hasSize(3));
        for (int i = 0; i < sortedRunResults.size(); i++) {
            RunResult currentImpl = sortedRunResults.get(i);
            // compare with the other implementation
            RunResult otherImpl = otherImplementationRunResults.get(i);

            assertThat(score(currentImpl), is(not(greaterThan(score(otherImpl) * 1.2))));
        }
    }

    private static double amountSize(RunResult r) {
        return parseInt(r.getParams().getParam("amountSize"));
    }

    static double score(RunResult r) {
        return r.getPrimaryResult().getScore();
    }
}
