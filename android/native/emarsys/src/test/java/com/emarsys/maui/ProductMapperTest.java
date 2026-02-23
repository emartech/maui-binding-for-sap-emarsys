package com.emarsys.maui;

import com.emarsys.maui.mapper.ProductMapper;
import com.emarsys.maui.model.EMSProduct;
import com.emarsys.predict.api.model.Product;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProductMapperTest {

    @Test
    public void testMap_withNull() {
        List<EMSProduct> result = ProductMapper.map(null);
        
        assertNull("Result should be null", result);
    }

    @Test
    public void testMap_withEmptyList() {
        List<EMSProduct> result = ProductMapper.map(new ArrayList<>());
        
        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty list", 0, result.size());
    }

    @Test
    public void testMap_withCompleteProduct() throws MalformedURLException {
        Map<String, String> customFields = new HashMap<>();
        customFields.put("color", "red");
        customFields.put("size", "L");

        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn("prod123");
        when(product.getTitle()).thenReturn("Test Product");
        when(product.getLinkUrl()).thenReturn("https://example.com/product");
        when(product.getFeature()).thenReturn("featured");
        when(product.getCohort()).thenReturn("cohort1");
        when(product.getCustomFields()).thenReturn(customFields);
        when(product.getImageUrl()).thenReturn(new URL("https://example.com/image.jpg"));
        when(product.getZoomImageUrl()).thenReturn(new URL("https://example.com/zoom.jpg"));
        when(product.getCategoryPath()).thenReturn("Electronics > Phones");
        when(product.getAvailable()).thenReturn(true);
        when(product.getProductDescription()).thenReturn("A great product");
        when(product.getPrice()).thenReturn(99.99f);
        when(product.getMsrp()).thenReturn(149.99f);
        when(product.getAlbum()).thenReturn("Greatest Hits");
        when(product.getActor()).thenReturn("John Doe");
        when(product.getArtist()).thenReturn("Jane Smith");
        when(product.getAuthor()).thenReturn("Bob Writer");
        when(product.getBrand()).thenReturn("BrandX");
        when(product.getYear()).thenReturn(2024);

        List<EMSProduct> result = ProductMapper.map(Collections.singletonList(product));

        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 1 product", 1, result.size());

        EMSProduct mappedProduct = result.get(0);
        assertEquals("ProductId should match", "prod123", mappedProduct.getProductId());
        assertEquals("Title should match", "Test Product", mappedProduct.getTitle());
        assertEquals("LinkUrl should match", "https://example.com/product", mappedProduct.getLinkUrl());
        assertEquals("Feature should match", "featured", mappedProduct.getFeature());
        assertEquals("Cohort should match", "cohort1", mappedProduct.getCohort());
        assertNotNull("CustomFields should not be null", mappedProduct.getCustomFields());
        assertEquals("CustomFields size should match", 2, mappedProduct.getCustomFields().size());
        assertEquals("CustomField color should match", "red", mappedProduct.getCustomFields().get("color"));
        assertEquals("CustomField size should match", "L", mappedProduct.getCustomFields().get("size"));
        assertNotNull("ImageUrl should not be null", mappedProduct.getImageUrl());
        assertEquals("ImageUrl should match", "https://example.com/image.jpg", mappedProduct.getImageUrl().toString());
        assertNotNull("ZoomImageUrl should not be null", mappedProduct.getZoomImageUrl());
        assertEquals("ZoomImageUrl should match", "https://example.com/zoom.jpg", mappedProduct.getZoomImageUrl().toString());
        assertEquals("CategoryPath should match", "Electronics > Phones", mappedProduct.getCategoryPath());
        assertEquals("Available should match", Boolean.TRUE, mappedProduct.getAvailable());
        assertEquals("ProductDescription should match", "A great product", mappedProduct.getProductDescription());
        assertEquals("Price should match", Float.valueOf(99.99f), mappedProduct.getPrice());
        assertEquals("Msrp should match", Float.valueOf(149.99f), mappedProduct.getMsrp());
        assertEquals("Album should match", "Greatest Hits", mappedProduct.getAlbum());
        assertEquals("Actor should match", "John Doe", mappedProduct.getActor());
        assertEquals("Artist should match", "Jane Smith", mappedProduct.getArtist());
        assertEquals("Author should match", "Bob Writer", mappedProduct.getAuthor());
        assertEquals("Brand should match", "BrandX", mappedProduct.getBrand());
        assertEquals("Year should match", Integer.valueOf(2024), mappedProduct.getYear());
    }

    @Test
    public void testMap_withMultipleProducts() {
        Map<String, String> customFields1 = new HashMap<>();
        customFields1.put("key1", "value1");

        Map<String, String> customFields2 = new HashMap<>();
        customFields2.put("key2", "value2");

        Product product1 = mock(Product.class);
        when(product1.getProductId()).thenReturn("prod1");
        when(product1.getTitle()).thenReturn("Product 1");
        when(product1.getLinkUrl()).thenReturn("https://example.com/product1");
        when(product1.getFeature()).thenReturn("feature1");
        when(product1.getCohort()).thenReturn("cohort1");
        when(product1.getCustomFields()).thenReturn(customFields1);
        when(product1.getImageUrl()).thenReturn(null);
        when(product1.getZoomImageUrl()).thenReturn(null);
        when(product1.getCategoryPath()).thenReturn("Category1");
        when(product1.getAvailable()).thenReturn(true);
        when(product1.getProductDescription()).thenReturn("Description 1");
        when(product1.getPrice()).thenReturn(10.0f);
        when(product1.getMsrp()).thenReturn(15.0f);
        when(product1.getAlbum()).thenReturn(null);
        when(product1.getActor()).thenReturn(null);
        when(product1.getArtist()).thenReturn(null);
        when(product1.getAuthor()).thenReturn(null);
        when(product1.getBrand()).thenReturn("Brand1");
        when(product1.getYear()).thenReturn(2023);

        Product product2 = mock(Product.class);
        when(product2.getProductId()).thenReturn("prod2");
        when(product2.getTitle()).thenReturn("Product 2");
        when(product2.getLinkUrl()).thenReturn("https://example.com/product2");
        when(product2.getFeature()).thenReturn("feature2");
        when(product2.getCohort()).thenReturn("cohort2");
        when(product2.getCustomFields()).thenReturn(customFields2);
        when(product2.getImageUrl()).thenReturn(null);
        when(product2.getZoomImageUrl()).thenReturn(null);
        when(product2.getCategoryPath()).thenReturn("Category2");
        when(product2.getAvailable()).thenReturn(false);
        when(product2.getProductDescription()).thenReturn("Description 2");
        when(product2.getPrice()).thenReturn(20.0f);
        when(product2.getMsrp()).thenReturn(25.0f);
        when(product2.getAlbum()).thenReturn(null);
        when(product2.getActor()).thenReturn(null);
        when(product2.getArtist()).thenReturn(null);
        when(product2.getAuthor()).thenReturn(null);
        when(product2.getBrand()).thenReturn("Brand2");
        when(product2.getYear()).thenReturn(2024);

        List<EMSProduct> result = ProductMapper.map(Arrays.asList(product1, product2));

        assertNotNull("Result should not be null", result);
        assertEquals("Result should have 2 products", 2, result.size());

        EMSProduct firstProduct = result.get(0);
        assertEquals("First product id should match", "prod1", firstProduct.getProductId());
        assertEquals("First product title should match", "Product 1", firstProduct.getTitle());
        assertEquals("First product brand should match", "Brand1", firstProduct.getBrand());

        EMSProduct secondProduct = result.get(1);
        assertEquals("Second product id should match", "prod2", secondProduct.getProductId());
        assertEquals("Second product title should match", "Product 2", secondProduct.getTitle());
        assertEquals("Second product brand should match", "Brand2", secondProduct.getBrand());
    }
}
