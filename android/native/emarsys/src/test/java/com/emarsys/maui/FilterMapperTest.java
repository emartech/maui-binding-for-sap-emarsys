package com.emarsys.maui;

import com.emarsys.maui.mapper.FilterMapper;
import com.emarsys.maui.model.EMSRecommendationFilter;
import com.emarsys.predict.api.model.RecommendationFilter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FilterMapperTest {

    @Test
    public void testMap_withNull() {
        List<RecommendationFilter> result = FilterMapper.map(null);
        
        assertNull("Result should be null", result);
    }

    @Test
    public void testMap_withEmptyList() {
        List<RecommendationFilter> result = FilterMapper.map(new ArrayList<>());
        
        assertNull("Result should be null", result);
    }

    @Test
    public void testMap_withIncludeFilters() {
        EMSRecommendationFilter filter1 = new EMSRecommendationFilter(
            "INCLUDE",
            "testField1",
            "IS",
            Collections.singletonList("value1")
        );
        EMSRecommendationFilter filter2 = new EMSRecommendationFilter(
            "INCLUDE",
            "testField2",
            "HAS",
            Collections.singletonList("value2")
        );
        EMSRecommendationFilter filter3 = new EMSRecommendationFilter(
            "INCLUDE",
            "testField3",
            "IN",
            Arrays.asList("value3", "value4")
        );
        EMSRecommendationFilter filter4 = new EMSRecommendationFilter(
            "INCLUDE",
            "testField4",
            "OVERLAPS",
            Arrays.asList("value5", "value6")
        );
        
        List<RecommendationFilter> result = FilterMapper.map(Arrays.asList(filter1, filter2, filter3, filter4));
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 4 filters", 4, result.size());

        assertEquals("Filter[0] type should be INCLUDE", "INCLUDE", result.get(0).getType());
        assertEquals("Filter[0] field should match", "testField1", result.get(0).getField());
        assertEquals("Filter[0] comparison should be IS", "IS", result.get(0).getComparison());
        assertEquals("Filter[0] expectations should match", Collections.singletonList("value1"), result.get(0).getExpectations());

        assertEquals("Filter[1] type should be INCLUDE", "INCLUDE", result.get(1).getType());
        assertEquals("Filter[1] field should match", "testField2", result.get(1).getField());
        assertEquals("Filter[1] comparison should be HAS", "HAS", result.get(1).getComparison());
        assertEquals("Filter[1] expectations should match", Collections.singletonList("value2"), result.get(1).getExpectations());

        assertEquals("Filter[2] type should be INCLUDE", "INCLUDE", result.get(2).getType());
        assertEquals("Filter[2] field should match", "testField3", result.get(2).getField());
        assertEquals("Filter[2] comparison should be IN", "IN", result.get(2).getComparison());
        assertEquals("Filter[2] expectations should match", Arrays.asList("value3", "value4"), result.get(2).getExpectations());

        assertEquals("Filter[3] type should be INCLUDE", "INCLUDE", result.get(3).getType());
        assertEquals("Filter[3] field should match", "testField4", result.get(3).getField());
        assertEquals("Filter[3] comparison should be OVERLAPS", "OVERLAPS", result.get(3).getComparison());
        assertEquals("Filter[3] expectations should match", Arrays.asList("value5", "value6"), result.get(3).getExpectations());
    }

    @Test
    public void testMap_withExcludeFilters() {
        EMSRecommendationFilter filter1 = new EMSRecommendationFilter(
            "EXCLUDE",
            "testField1",
            "IS",
            Collections.singletonList("value1")
        );
        EMSRecommendationFilter filter2 = new EMSRecommendationFilter(
            "EXCLUDE",
            "testField2",
            "HAS",
            Collections.singletonList("value2")
        );
        EMSRecommendationFilter filter3 = new EMSRecommendationFilter(
            "EXCLUDE",
            "testField3",
            "IN",
            Arrays.asList("value3", "value4")
        );
        EMSRecommendationFilter filter4 = new EMSRecommendationFilter(
            "EXCLUDE",
            "testField4",
            "OVERLAPS",
            Arrays.asList("value5", "value6")
        );
        
        List<RecommendationFilter> result = FilterMapper.map(Arrays.asList(filter1, filter2, filter3, filter4));
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 4 filters", 4, result.size());

        assertEquals("Filter[0] type should be EXCLUDE", "EXCLUDE", result.get(0).getType());
        assertEquals("Filter[0] field should match", "testField1", result.get(0).getField());
        assertEquals("Filter[0] comparison should be IS", "IS", result.get(0).getComparison());
        assertEquals("Filter[0] expectations should match", Collections.singletonList("value1"), result.get(0).getExpectations());

        assertEquals("Filter[1] type should be EXCLUDE", "EXCLUDE", result.get(1).getType());
        assertEquals("Filter[1] field should match", "testField2", result.get(1).getField());
        assertEquals("Filter[1] comparison should be HAS", "HAS", result.get(1).getComparison());
        assertEquals("Filter[1] expectations should match", Collections.singletonList("value2"), result.get(1).getExpectations());

        assertEquals("Filter[2] type should be EXCLUDE", "EXCLUDE", result.get(2).getType());
        assertEquals("Filter[2] field should match", "testField3", result.get(2).getField());
        assertEquals("Filter[2] comparison should be IN", "IN", result.get(2).getComparison());
        assertEquals("Filter[2] expectations should match", Arrays.asList("value3", "value4"), result.get(2).getExpectations());

        assertEquals("Filter[3] type should be EXCLUDE", "EXCLUDE", result.get(3).getType());
        assertEquals("Filter[3] field should match", "testField4", result.get(3).getField());
        assertEquals("Filter[3] comparison should be OVERLAPS", "OVERLAPS", result.get(3).getComparison());
        assertEquals("Filter[3] expectations should match", Arrays.asList("value5", "value6"), result.get(3).getExpectations());
    }
}
