package com.emarsys.maui;

import com.emarsys.maui.mapper.CartItemMapper;
import com.emarsys.maui.model.EMSCartItem;
import com.emarsys.predict.api.model.PredictCartItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CartItemMapperTest {

    @Test
    public void testMap_withEmptyList() {
        List<PredictCartItem> result = CartItemMapper.map(new ArrayList<>());

        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty list", 0, result.size());
    }

    @Test
    public void testMap_withSingleItem() {
        EMSCartItem item = new EMSCartItem("item123", 19.99, 2.0);
        
        List<PredictCartItem> result = CartItemMapper.map(Collections.singletonList(item));

        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 1 item", 1, result.size());
        
        PredictCartItem mappedItem = result.get(0);
        assertEquals("ItemId should match", "item123", mappedItem.getItemId());
        assertEquals("Price should match", 19.99, mappedItem.getPrice(), 0.001);
        assertEquals("Quantity should match", 2.0, mappedItem.getQuantity(), 0.001);
    }

    @Test
    public void testMap_withMultipleItems() {
        EMSCartItem item1 = new EMSCartItem("item1", 10.5, 1.0);
        EMSCartItem item2 = new EMSCartItem("item2", 25.0, 3.0);
        EMSCartItem item3 = new EMSCartItem("item3", 5.99, 10.0);
        
        List<PredictCartItem> result = CartItemMapper.map(Arrays.asList(item1, item2, item3));

        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 3 items", 3, result.size());
        
        assertEquals("First item id should match", "item1", result.get(0).getItemId());
        assertEquals("First item price should match", 10.5, result.get(0).getPrice(), 0.001);
        assertEquals("First item quantity should match", 1.0, result.get(0).getQuantity(), 0.001);
        
        assertEquals("Second item id should match", "item2", result.get(1).getItemId());
        assertEquals("Second item price should match", 25.0, result.get(1).getPrice(), 0.001);
        assertEquals("Second item quantity should match", 3.0, result.get(1).getQuantity(), 0.001);
        
        assertEquals("Third item id should match", "item3", result.get(2).getItemId());
        assertEquals("Third item price should match", 5.99, result.get(2).getPrice(), 0.001);
        assertEquals("Third item quantity should match", 10.0, result.get(2).getQuantity(), 0.001);
    }
}
