package com.emarsys.maui.model;

import androidx.annotation.NonNull;
import java.util.List;

public class EMSRecommendationFilter {

    private final String type;
    private final String field;
    private final String comparison;
    private final List<String> expectations;

    public EMSRecommendationFilter(@NonNull String type, @NonNull String field, @NonNull String comparison, @NonNull List<String> expectations) {
        this.type = type;
        this.field = field;
        this.comparison = comparison;
        this.expectations = expectations;
    }

    public @NonNull String getType() { return type; }

    public @NonNull String getField() { return field; }

    public @NonNull String getComparison() { return comparison; }

    public @NonNull List<String> getExpectations() { return expectations; }

}
