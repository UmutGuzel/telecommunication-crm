package com.gygy.contractservice.model.enums;

public enum BillingCycleType {
    MONTHLY(1),
    QUARTERLY(3),
    SEMI_ANNUAL(6),
    ANNUAL(12);

    private final int months;

    BillingCycleType(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }

    public static BillingCycleType fromMonths(int months) {
        for (BillingCycleType type : values()) {
            if (type.getMonths() == months) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown billing cycle for months: " + months);
    }
}
