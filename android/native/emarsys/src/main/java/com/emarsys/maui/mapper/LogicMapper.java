package com.emarsys.maui.mapper;

import androidx.annotation.NonNull;
import com.emarsys.maui.model.EMSLogic;
import com.emarsys.predict.api.model.Logic;
import com.emarsys.predict.api.model.RecommendationLogic;

public class LogicMapper {

    public static @NonNull Logic map(@NonNull EMSLogic logic) {
        switch (logic.getName()) {
            case "SEARCH":
                if (logic.getQuery() != null) { return RecommendationLogic.search(logic.getQuery()); }
                return RecommendationLogic.search();
            case "CART":
                if (logic.getCartItems() != null) { return RecommendationLogic.cart(CartItemMapper.map(logic.getCartItems())); }
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
}
