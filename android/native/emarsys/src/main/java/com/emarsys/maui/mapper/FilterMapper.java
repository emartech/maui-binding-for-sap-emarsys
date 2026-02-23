package com.emarsys.maui.mapper;

import com.emarsys.maui.model.EMSRecommendationFilter;
import com.emarsys.predict.api.model.RecommendationFilter;
import java.util.ArrayList;
import java.util.List;

public class FilterMapper {

    public static List<RecommendationFilter> map(List<EMSRecommendationFilter> filters) {
        if (filters == null || filters.isEmpty()) { return null; }
        List<RecommendationFilter> _filters = new ArrayList<>();
        for (int i = 0; i < filters.size(); i++) {
            _filters.add(mapFilter(filters.get(i)));
        }
        return _filters;
    }

    private static RecommendationFilter mapFilter(EMSRecommendationFilter filter) {
        switch (filter.getType()) {
            case "INCLUDE":
                switch (filter.getComparison()) {
                    case "IS":
                        return RecommendationFilter.include(filter.getField()).isValue(filter.getExpectations().get(0));
                    case "IN":
                        return RecommendationFilter.include(filter.getField()).inValues(filter.getExpectations());
                    case "HAS":
                        return RecommendationFilter.include(filter.getField()).hasValue(filter.getExpectations().get(0));
                    case "OVERLAPS":
                        return RecommendationFilter.include(filter.getField()).overlapsValues(filter.getExpectations());
                    default:
                        break;
                }
            case "EXCLUDE":
                switch (filter.getComparison()) {
                    case "IS":
                        return RecommendationFilter.exclude(filter.getField()).isValue(filter.getExpectations().get(0));
                    case "IN":
                        return RecommendationFilter.exclude(filter.getField()).inValues(filter.getExpectations());
                    case "HAS":
                        return RecommendationFilter.exclude(filter.getField()).hasValue(filter.getExpectations().get(0));
                    case "OVERLAPS":
                        return RecommendationFilter.exclude(filter.getField()).overlapsValues(filter.getExpectations());
                    default:
                        break;
                }
            default:
                break;
        }
        return RecommendationFilter.include(filter.getField()).isValue(filter.getExpectations().get(0));
    }
}
