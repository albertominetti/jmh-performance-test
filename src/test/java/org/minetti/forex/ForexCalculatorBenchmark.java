package org.minetti.forex;

import org.minetti.forex.model.Amount;
import org.minetti.forex.model.CurrencyPair;
import org.minetti.forex.model.FxRate;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Fork(1)
@Warmup(iterations = 5) // suggested 5
@Measurement(iterations = 10) // suggested 10
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ForexCalculatorBenchmark {

    private final FakePriceService priceServiceClient = new FakePriceService();
    private final ForexCalculator forexCalculator = new ForexCalculator(priceServiceClient);
    private Collection<FxRate> fxRates;

    @Param({"100", "1000", "10000"})
    private int amountSize;

    @State(Scope.Benchmark)
    public static class RequestHolder {

        private Random rand = new Random();
        private static final List<String> CURRENCIES = asList("EUR", "CHF", "JPY", "PLN", "USD");

        List<Amount> amounts;
        String targetCcy = randomCurrency();

        @Setup(Level.Iteration)
        public void setup(ForexCalculatorBenchmark state) {
            amounts = new ArrayList<>(state.amountSize);
            for (int i = 0; i < state.amountSize; i++) {
                amounts.add(new Amount(randomCurrency(), randomBigDecimal()));
            }

            state.fxRates = amounts.stream()
                    .map(Amount::getCcy).distinct()
                    .filter(currency -> !currency.equals(targetCcy))
                    .map(currency -> new FxRate(currency, targetCcy, randomBigDecimal()))
                    .collect(toList());
        }

        private BigDecimal randomBigDecimal() {
            return new BigDecimal(BigInteger.valueOf(rand.nextInt(100001)), 2);
        }

        private String randomCurrency() {
            return CURRENCIES.get(rand.nextInt(CURRENCIES.size()));
        }
    }


    @Benchmark
    public BigDecimal onCurrentImplementation(RequestHolder requestHolder) {
        return forexCalculator.sumInTargetCurrency(requestHolder.amounts, requestHolder.targetCcy);
    }

    @Benchmark
    public BigDecimal onAnotherImplementation(RequestHolder requestHolder) {
        return convertAllAmountsIntoTargetThenSum(requestHolder.amounts, requestHolder.targetCcy);
    }


    public BigDecimal convertAllAmountsIntoTargetThenSum(List<Amount> amounts, String targetCcy) {
        boolean validAmounts = amounts.stream().allMatch(a -> isNotBlank(a.getCcy()) && a.getValue() != null);
        if (!validAmounts) {
            throw new IllegalArgumentException("Invalid amounts");
        }

        BigDecimal total = BigDecimal.ZERO;

        Set<CurrencyPair> fxRatesRequest = amounts.stream()
                .map(Amount::getCcy).distinct()
                .filter(currency -> !currency.equals(targetCcy))
                .map(ccy -> new CurrencyPair(ccy, targetCcy))
                .collect(Collectors.toSet());


        if (!fxRatesRequest.isEmpty()) {
            Map<String, BigDecimal> fxMap = priceServiceClient.getFxRates(fxRatesRequest).stream()
                    .collect(toMap(FxRate::getBaseCurrency, FxRate::getValue));

            total = total.add(amounts.stream()
                    .filter(a -> !a.getCcy().equals(targetCcy))
                    // BEFORE: conversion into target currency
                    .map(amount -> amount.getValue().multiply(fxMap.get(amount.getCcy())))
                    // AFTER: sum together
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        // add also all the amount that were already in target currency
        total = total.add(amounts.stream()
                .filter(a -> a.getCcy().equals(targetCcy))
                .map(Amount::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return total;
    }


    public class FakePriceService implements PriceServiceClient {
        @Override
        public Collection<FxRate> getFxRates(Set<CurrencyPair> currencyPairs) {
            return fxRates;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ForexCalculatorBenchmark.class.getName() + "\\.")
                .build();

        new Runner(opt).run();
    }

    /*
Using VM version: JDK 1.8.0_221 on HP Omen with i7-8750H CPU @ 2.20GHz
Benchmark                                         (amountSize)  Mode  Cnt     Score     Error  Units
ForexCalculatorBenchmark.onCurrentImplementation           100  avgt   10     6,456 ±   0,386  us/op
ForexCalculatorBenchmark.onCurrentImplementation          1000  avgt   10    41,121 ±   8,822  us/op
ForexCalculatorBenchmark.onCurrentImplementation         10000  avgt   10   390,025 ± 104,373  us/op
ForexCalculatorBenchmark.onAnotherImplementation           100  avgt   10    11,957 ±   1,671  us/op
ForexCalculatorBenchmark.onAnotherImplementation          1000  avgt   10   111,211 ±   3,956  us/op
ForexCalculatorBenchmark.onAnotherImplementation         10000  avgt   10  1852,680 ± 308,549  us/op

In Jenkins
[2020-04-06T13:27:20.286Z] Benchmark                                         (amountSize)  Mode  Cnt     Score    Error  Units
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onCurrentImplementation           100  avgt   10     5.099 ?  0.238  us/op
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onCurrentImplementation          1000  avgt   10    43.946 ?  4.446  us/op
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onCurrentImplementation         10000  avgt   10   378.300 ? 15.998  us/op
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onAnotherImplementation           100  avgt   10    13.371 ?  1.055  us/op
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onAnotherImplementation          1000  avgt   10   122.651 ?  8.812  us/op
[2020-04-06T13:27:20.286Z] ForexCalculatorBenchmark.onAnotherImplementation         10000  avgt   10  1325.586 ? 62.417  us/op
     */

}
