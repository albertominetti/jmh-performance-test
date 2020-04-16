package org.minetti.forex;

import org.minetti.forex.model.CurrencyPair;
import org.minetti.forex.model.FxRate;

import java.util.Collection;
import java.util.Set;

public interface PriceServiceClient {
    Collection<FxRate> getFxRates(Set<CurrencyPair> fxRatesRequest);
}
