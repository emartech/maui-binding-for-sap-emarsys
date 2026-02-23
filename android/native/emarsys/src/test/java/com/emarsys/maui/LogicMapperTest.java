package com.emarsys.maui;

import com.emarsys.maui.mapper.LogicMapper;
import com.emarsys.maui.model.EMSCartItem;
import com.emarsys.maui.model.EMSLogic;
import com.emarsys.predict.api.model.Logic;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class LogicMapperTest {

    @Test
    public void testMapLogic_withSearchLogic_withSearchTerm() {
        EMSLogic logic = new EMSLogic("SEARCH", "testSearchTerm", null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be SEARCH", "SEARCH", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertEquals("Data should contain search term", "testSearchTerm", result.getData().get("q"));
    }

    @Test
    public void testMapLogic_withSearchLogic_withoutSearchTerm() {
        EMSLogic logic = new EMSLogic("SEARCH", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be SEARCH", "SEARCH", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withCartLogic_withoutItems() {
        EMSLogic logic = new EMSLogic("CART", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be CART", "CART", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withCartLogic_withItems() {
        EMSCartItem item1 = new EMSCartItem("testItemId1", 123.0, 234.0);
        EMSCartItem item2 = new EMSCartItem("testItemId2", 456.0, 567.0);
        EMSLogic logic = new EMSLogic("CART", null, Arrays.asList(item1, item2), null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be CART", "CART", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should contain cart items key", result.getData().containsKey("ca"));
    }

    @Test
    public void testMapLogic_withCategoryLogic_withCategoryPath() {
        EMSLogic logic = new EMSLogic("CATEGORY", "testCategoryPath", null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be CATEGORY", "CATEGORY", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertEquals("Data should contain category path", "testCategoryPath", result.getData().get("vc"));
    }

    @Test
    public void testMapLogic_withCategoryLogic_withoutCategoryPath() {
        EMSLogic logic = new EMSLogic("CATEGORY", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be CATEGORY", "CATEGORY", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withRelatedLogic_withItemId() {
        EMSLogic logic = new EMSLogic("RELATED", "testItemId", null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be RELATED", "RELATED", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertEquals("Data should contain item id", "i:testItemId", result.getData().get("v"));
    }

    @Test
    public void testMapLogic_withRelatedLogic_withoutItemId() {
        EMSLogic logic = new EMSLogic("RELATED", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be RELATED", "RELATED", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withAlsoBoughtLogic_withItemId() {
        EMSLogic logic = new EMSLogic("ALSO_BOUGHT", "testItemId", null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be ALSO_BOUGHT", "ALSO_BOUGHT", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertEquals("Data should contain item id", "i:testItemId", result.getData().get("v"));
    }

    @Test
    public void testMapLogic_withAlsoBoughtLogic_withoutItemId() {
        EMSLogic logic = new EMSLogic("ALSO_BOUGHT", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be ALSO_BOUGHT", "ALSO_BOUGHT", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withPopularLogic_withCategoryPath() {
        EMSLogic logic = new EMSLogic("POPULAR", "testCategoryPath", null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be POPULAR", "POPULAR", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertEquals("Data should contain category path", "testCategoryPath", result.getData().get("vc"));
    }

    @Test
    public void testMapLogic_withPopularLogic_withoutCategoryPath() {
        EMSLogic logic = new EMSLogic("POPULAR", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be POPULAR", "POPULAR", result.getLogicName());
        assertNotNull("Data should not be null", result.getData());
        assertTrue("Data should be empty", result.getData().isEmpty());
    }

    @Test
    public void testMapLogic_withPersonalLogic_withVariants() {
        EMSLogic logic = new EMSLogic("PERSONAL", null, null, Arrays.asList("testVariant1", "testVariant2"));
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be PERSONAL", "PERSONAL", result.getLogicName());
        assertNotNull("Variants should not be null", result.getVariants());
        assertEquals("Variants should have 2 items", 2, result.getVariants().size());
        assertEquals("First variant should match", "testVariant1", result.getVariants().get(0));
        assertEquals("Second variant should match", "testVariant2", result.getVariants().get(1));
    }

    @Test
    public void testMapLogic_withPersonalLogic_withoutVariants() {
        EMSLogic logic = new EMSLogic("PERSONAL", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be PERSONAL", "PERSONAL", result.getLogicName());
        assertNotNull("Variants should not be null", result.getVariants());
        assertTrue("Variants should be empty", result.getVariants().isEmpty());
    }

    @Test
    public void testMapLogic_withHomeLogic_withVariants() {
        EMSLogic logic = new EMSLogic("HOME", null, null, Arrays.asList("testVariant1", "testVariant2"));
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be HOME", "HOME", result.getLogicName());
        assertNotNull("Variants should not be null", result.getVariants());
        assertEquals("Variants should have 2 items", 2, result.getVariants().size());
        assertEquals("First variant should match", "testVariant1", result.getVariants().get(0));
        assertEquals("Second variant should match", "testVariant2", result.getVariants().get(1));
    }

    @Test
    public void testMapLogic_withHomeLogic_withoutVariants() {
        EMSLogic logic = new EMSLogic("HOME", null, null, null);
        
        Logic result = LogicMapper.map(logic);
        
        assertNotNull("Result should not be null", result);
        assertEquals("Logic name should be HOME", "HOME", result.getLogicName());
        assertNotNull("Variants should not be null", result.getVariants());
        assertTrue("Variants should be empty", result.getVariants().isEmpty());
    }
}
