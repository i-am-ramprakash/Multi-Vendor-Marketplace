package com.marketplace.commission.domain.valueobject;

import lombok.Getter;

@Getter
public enum CommissionType {
    PERCENTAGE("Percentage", "Commission as percentage of sale"),
    FIXED("Fixed Amount", "Fixed amount per transaction"),
    TIERED("Tiered", "Different rates based on volume");

    private final String displayName;
    private final String description;

    CommissionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}