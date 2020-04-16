package org.minetti.forex;


import org.apache.commons.lang3.StringUtils;
import org.minetti.forex.model.Amount;
import org.minetti.forex.model.CurrencyPair;
import org.minetti.forex.model.FxRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ForexCalculator {

    private static final Logger log = LoggerFactory.getLogger(ForexCalculator.class);

    private final PriceServiceClient priceServiceClient;

    public ForexCalculator(PriceServiceClient priceServiceClient) {
        this.priceServiceClient = priceServiceClient;
    }

    public BigDecimal sumInTargetCurrency(List<Amount> amounts, String targetCcy) {
        validateAmounts(amounts);

        Map<String, BigDecimal> totalsForCurrency = amounts.stream()
                .collect(Collectors.toMap(Amount::getCcy, Amount::getValue, BigDecimal::add));

        BigDecimal total = BigDecimal.ZERO;

        Set<CurrencyPair> fxRatesRequest = createFxRequestWithoutTargetCcy(totalsForCurrency, targetCcy);

        if (!fxRatesRequest.isEmpty()) {
            total = total.add(calculateTotalForNonTargetCcy(totalsForCurrency, fxRatesRequest));
        }

        if (totalsForCurrency.containsKey(targetCcy)) {
            total = total.add(totalsForCurrency.get(targetCcy));
        }

        return total;
    }


    private void validateAmounts(List<Amount> amounts) {
        boolean validAmounts = amounts.stream().allMatch(a -> isNotBlank(a.getCcy()) && a.getValue() != null);

        if (!validAmounts) {
            log.error("Invalid amounts to calculate the rates: {}", amounts);
            throw new IllegalArgumentException("Invalid amounts");
        }
    }

    private BigDecimal calculateTotalForNonTargetCcy(Map<String, BigDecimal> totalsForCurrency,
                                                     Set<CurrencyPair> fxRatesRequest) {
        Collection<FxRate> fxRates = retrieveFxRates(fxRatesRequest);

        return fxRates.stream()
                .map(rate -> totalsForCurrency.get(rate.getBaseCurrency()).multiply(rate.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<CurrencyPair> createFxRequestWithoutTargetCcy(Map<String, BigDecimal> totalsForCurrency,
                                                              String targetCcy) {
        return totalsForCurrency.keySet().stream()
                .filter(ccy -> !StringUtils.equals(ccy, targetCcy))
                .map(ccy -> new CurrencyPair(ccy, targetCcy))
                .collect(Collectors.toSet());
    }

    private Collection<FxRate> retrieveFxRates(Set<CurrencyPair> fxRatesRequest) {
        log.info("Querying price-service for {} rates", fxRatesRequest.size());
        log.debug("Requesting fx rate from price-service for currencies: {}", fxRatesRequest);
        Collection<FxRate> fxRates = priceServiceClient.getFxRates(fxRatesRequest);
        log.debug("Retrieved fx rates: {}", fxRates);

        if (fxRatesRequest.size() != fxRates.size()) {
            String message = String.format("Asked for %d fx rates, but got %d fx rates",
                    fxRatesRequest.size(), fxRates.size());
            log.error(message);
            log.warn("Details: request {}, response {}", fxRatesRequest, fxRates);
            throw new IllegalArgumentException(message);
        }
        return fxRates;
    }

}
