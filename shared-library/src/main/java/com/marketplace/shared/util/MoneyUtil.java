package com.marketplace.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {

    private MoneyUtil() {
    }

    public static BigDecimal calculate(BigDecimal amount, BigDecimal percentage) {
        if (amount == null || percentage == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(percentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) return BigDecimal.ZERO;
        if (b == null) return a;
        return a.subtract(b);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) return b != null ? b : BigDecimal.ZERO;
        if (b == null) return a;
        return a.add(b);
    }

    public static BigDecimal zeroIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    public static BigDecimal scale(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO;
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
