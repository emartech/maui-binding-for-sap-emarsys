package com.emarsys.maui.mapper;

import androidx.annotation.NonNull;
import com.emarsys.maui.model.EMSCartItem;
import com.emarsys.maui.model.EMSLogic;
import com.emarsys.maui.model.EMSProduct;
import com.emarsys.maui.model.EMSRecommendationFilter;
import com.emarsys.predict.api.model.Logic;
import com.emarsys.predict.api.model.PredictCartItem;
import com.emarsys.predict.api.model.Product;
import com.emarsys.predict.api.model.RecommendationFilter;
import com.emarsys.predict.api.model.RecommendationLogic;
import java.util.ArrayList;
import java.util.List;

public class PredictMapper {

    public static @NonNull List<PredictCartItem> mapCartItems(@NonNull List<EMSCartItem> items) {
        List<PredictCartItem> _items = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            EMSCartItem item = items.get(i);
            _items.add(new PredictCartItem(item.getItemId(), item.getPrice(), item.getQuantity()));
        }
        return _items;
    }

    public static @NonNull Logic mapLogic(@NonNull EMSLogic logic) {
        switch (logic.getName()) {
            case "SEARCH":
                if (logic.getQuery() != null) { return RecommendationLogic.search(logic.getQuery()); }
                return RecommendationLogic.search();
            case "CART":
                if (logic.getCartItems() != null) { return RecommendationLogic.cart(mapCartItems(logic.getCartItems())); }
                return RecommendationLogic.cart();
            case "RELATED":
                if (logic.getQuery() != null) { return RecommendationLogic.related(logic.getQuery()); }
                return RecommendationLogic.related();
            case "CATEGORY":
                if (logic.getQuery() != null) { return RecommendationLogic.category(logic.getQuery()); }
                return RecommendationLogic.category();
            case "ALSO_BOUGHT":
                if (logic.getQuery() != null) { return RecommendationLogic.alsoBought(logic.getQuery()); }
                return RecommendationLogic.alsoBought();
            case "POPULAR":
                if (logic.getQuery() != null) { return RecommendationLogic.popular(logic.getQuery()); }
                return RecommendationLogic.popular();
            case "PERSONAL":
                if (logic.getVariants() != null) { return RecommendationLogic.personal(logic.getVariants()); }
                return RecommendationLogic.personal();
            case "HOME":
                if (logic.getVariants() != null) { return RecommendationLogic.home(logic.getVariants()); }
                return RecommendationLogic.home();
            default:
                return RecommendationLogic.search();
        }
    }

    public static List<RecommendationFilter> mapFilters(List<EMSRecommendationFilter> filters) {
        if (filters == null || filters.isEmpty()) { return null; }
        List<RecommendationFilter> _filters = new ArrayList<>();
        for (int i = 0; i < filters.size(); i++) {
            _filters.add(mapFilter(filters.get(i)));
        }
        return _filters;
    }

    public static RecommendationFilter mapFilter(EMSRecommendationFilter filter) {
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

    public static List<EMSProduct> mapProducts(List<Product> products) {
        if (products == null) { return null; }
        List<EMSProduct> _products = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            _products.add(new EMSProduct(
                    p.getProductId(), p.getTitle(), p.getLinkUrl(), p.getFeature(), p.getCohort(), p.getCustomFields(),
                    p.getImageUrl(), p.getZoomImageUrl(), p.getCategoryPath(), p.getAvailable(),
                    p.getProductDescription(), p.getPrice(), p.getMsrp(),
                    p.getAlbum(), p.getActor(), p.getArtist(), p.getAuthor(), p.getBrand(), p.getYear()
            ));
        }
        return _products;
    }

}
