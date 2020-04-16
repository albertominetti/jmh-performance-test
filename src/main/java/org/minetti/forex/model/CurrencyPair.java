package org.minetti.forex.model;

import java.util.Objects;

public class CurrencyPair {
    private final String baseCcy;
    private final String targetCcy;

    public CurrencyPair(String baseCcy, String targetCcy) {
        this.baseCcy = baseCcy;
        this.targetCcy = targetCcy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(baseCcy, that.baseCcy) &&
                Objects.equals(targetCcy, that.targetCcy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCcy, targetCcy);
    }

    @Override
    public String toString() {
        return baseCcy + "/" + targetCcy;
    }
}
