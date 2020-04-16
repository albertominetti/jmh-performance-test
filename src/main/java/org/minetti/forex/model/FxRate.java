package org.minetti.forex.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class FxRate {
    private final String baseCurrency;
    private final String targetCcy;
    private final BigDecimal value;

    public FxRate(String baseCurrency, String targetCcy, BigDecimal value) {
        this.baseCurrency = baseCurrency;
        this.targetCcy = targetCcy;
        this.value = value;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getTargetCcy() {
        return targetCcy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FxRate fxRate = (FxRate) o;
        return Objects.equals(baseCurrency, fxRate.baseCurrency) &&
                Objects.equals(targetCcy, fxRate.targetCcy) &&
                Objects.equals(value, fxRate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, targetCcy, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FxRate.class.getSimpleName() + "[", "]")
                .add("baseCurrency='" + baseCurrency + "'")
                .add("targetCcy='" + targetCcy + "'")
                .add("value=" + value)
                .toString();
    }
}
