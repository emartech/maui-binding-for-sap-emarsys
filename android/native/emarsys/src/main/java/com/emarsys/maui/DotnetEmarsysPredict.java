package com.emarsys.maui;

import androidx.annotation.NonNull;
import com.emarsys.Emarsys;
import com.emarsys.core.api.result.ResultListener;
import com.emarsys.core.api.result.Try;
import com.emarsys.maui.mapper.PredictMapper;
import com.emarsys.maui.model.EMSCartItem;
import com.emarsys.maui.model.EMSLogic;
import com.emarsys.maui.model.EMSProduct;
import com.emarsys.maui.model.EMSRecommendationFilter;
import com.emarsys.predict.api.model.Logic;
import com.emarsys.predict.api.model.Product;
import com.emarsys.predict.api.model.RecommendationFilter;
import java.util.List;
import java.util.Map;

public class DotnetEmarsysPredict {

    public static @NonNull EMSCartItem buildCartItem(@NonNull String itemId, double price, double quantity) {
        return new EMSCartItem(itemId, price, quantity);
    }

    public static void trackCart(@NonNull List<EMSCartItem> items) {
        Emarsys.getPredict().trackCart(PredictMapper.mapCartItems(items));
    }

    public static void trackPurchase(@NonNull String orderId, @NonNull List<EMSCartItem> items) {
        Emarsys.getPredict().trackPurchase(orderId, PredictMapper.mapCartItems(items));
    }

    public static void trackItemView(@NonNull String itemId) {
        Emarsys.getPredict().trackItemView(itemId);
    }

    public static void trackCategoryView(@NonNull String categoryPath) {
        Emarsys.getPredict().trackCategoryView(categoryPath);
    }

    public static void trackSearchTerm(@NonNull String searchTerm) {
        Emarsys.getPredict().trackSearchTerm(searchTerm);
    }

    public static void trackTag(@NonNull String tag, Map<String, String> attributes) {
        Emarsys.getPredict().trackTag(tag, attributes);
    }

    public static @NonNull EMSLogic buildLogic(@NonNull String name, String query, List<EMSCartItem> cartItems, List<String> variants) {
        return new EMSLogic(name, query, cartItems, variants);
    }

    public static @NonNull EMSRecommendationFilter buildFilter(
            @NonNull String type, @NonNull String field, @NonNull String comparison, @NonNull List<String> expectations) {
        return new EMSRecommendationFilter(type, field, comparison, expectations);
    }

    public interface RecommendProductsCompletionListener {
        void onCompleted(List<EMSProduct> products, Throwable errorCause);
    }

    public static void recommendProducts(@NonNull EMSLogic logic, List<EMSRecommendationFilter> filters, Integer limit, String availabilityZone,
                                         @NonNull RecommendProductsCompletionListener completionListener) {
        Logic _logic = PredictMapper.mapLogic(logic);
        List<RecommendationFilter> _filters = PredictMapper.mapFilters(filters);
        ResultListener<Try<List<Product>>> resultListener = (result) -> completionListener.onCompleted(PredictMapper.mapProducts(result.getResult()), result.getErrorCause());

        if (_filters != null && limit != null && availabilityZone != null) {
            Emarsys.getPredict().recommendProducts(_logic, _filters, limit, availabilityZone, resultListener);
        } else if (_filters != null && limit != null) {
            Emarsys.getPredict().recommendProducts(_logic, _filters, limit, resultListener);
        } else if (_filters != null && availabilityZone != null) {
            Emarsys.getPredict().recommendProducts(_logic, _filters, availabilityZone, resultListener);
        } else if (limit != null && availabilityZone != null) {
            Emarsys.getPredict().recommendProducts(_logic, limit, availabilityZone, resultListener);
        } else if (_filters != null) {
            Emarsys.getPredict().recommendProducts(_logic, _filters, resultListener);
        } else if (limit != null) {
            Emarsys.getPredict().recommendProducts(_logic, limit, resultListener);
        } else if (availabilityZone != null) {
            Emarsys.getPredict().recommendProducts(_logic, availabilityZone, resultListener);
        } else {
            Emarsys.getPredict().recommendProducts(_logic, resultListener);
        }
    }

    public static void trackRecommendationClick(@NonNull EMSProduct product) {
        Emarsys.getPredict().trackRecommendationClick(new Product(
                product.getProductId(), product.getTitle(), product.getLinkUrl(),
                product.getFeature(), product.getCohort(), product.getCustomFields(),
                // These fields are not used for tracking recommendation click
                null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, null
        ));
    }

}
