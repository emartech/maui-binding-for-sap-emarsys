package com.emarsys.maui.mapper;

import com.emarsys.maui.model.EMSProduct;
import com.emarsys.predict.api.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static List<EMSProduct> map(List<Product> products) {
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
