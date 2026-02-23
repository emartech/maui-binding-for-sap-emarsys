package com.emarsys.maui.model;

import androidx.annotation.NonNull;

public class EMSCartItem {

    private final String itemId;
    private final double price;
    private final double quantity;

    public EMSCartItem(@NonNull String itemId, double price, double quantity) {
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
    }

    public @NonNull String getItemId() { return itemId; }

    public double getPrice() { return price; }

    public double getQuantity() { return quantity; }

}
