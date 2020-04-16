package org.minetti.forex.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Amount {
    private final BigDecimal value;
    private final String ccy;

    public Amount(String ccy, BigDecimal value) {
        this.ccy = ccy;
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCcy() {
        return ccy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value) &&
                Objects.equals(ccy, amount.ccy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, ccy);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Amount.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .add("ccy='" + ccy + "'")
                .toString();
    }
}
