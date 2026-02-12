package com.emarsys.maui.mapper;

import androidx.annotation.NonNull;
import com.emarsys.maui.model.EMSCartItem;
import com.emarsys.predict.api.model.PredictCartItem;
import java.util.ArrayList;
import java.util.List;

public class CartItemMapper {

    public static @NonNull List<PredictCartItem> map(@NonNull List<EMSCartItem> items) {
        List<PredictCartItem> _items = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            EMSCartItem item = items.get(i);
            _items.add(new PredictCartItem(item.getItemId(), item.getPrice(), item.getQuantity()));
        }
        return _items;
    }
}
