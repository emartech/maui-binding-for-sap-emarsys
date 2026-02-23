package com.emarsys.maui.model;

import androidx.annotation.NonNull;
import java.net.URL;
import java.util.Map;

public class EMSProduct {

    private final String productId;
    private final String title;
    private final String linkUrl;
    private final String feature;
    private final String cohort;
    private final Map<String, String> customFields;
    private final URL imageUrl;
    private final URL zoomImageUrl;
    private final String categoryPath;
    private final Boolean available;
    private final String productDescription;
    private final Float price;
    private final Float msrp;
    private final String album;
    private final String actor;
    private final String artist;
    private final String author;
    private final String brand;
    private final Integer year;

    public EMSProduct(@NonNull String productId, @NonNull String title, @NonNull String linkUrl,
                      @NonNull String feature, @NonNull String cohort, @NonNull Map<String, String> customFields,
                      URL imageUrl, URL zoomImageUrl, String categoryPath, Boolean available, String productDescription, Float price, Float msrp,
                      String album, String actor, String artist, String author, String brand, Integer year) {
        this.productId = productId;
        this.title = title;
        this.linkUrl = linkUrl;
        this.feature = feature;
        this.cohort = cohort;
        this.customFields = customFields;
        this.imageUrl = imageUrl;
        this.zoomImageUrl = zoomImageUrl;
        this.categoryPath = categoryPath;
        this.available = available;
        this.productDescription = productDescription;
        this.price = price;
        this.msrp = msrp;
        this.album = album;
        this.actor = actor;
        this.artist = artist;
        this.author = author;
        this.brand = brand;
        this.year = year;
    }

    public @NonNull String getProductId() { return productId; }

    public @NonNull String getTitle() { return title; }

    public @NonNull String getLinkUrl() { return linkUrl; }

    public @NonNull String getFeature() { return feature; }

    public @NonNull String getCohort() { return cohort; }

    public @NonNull Map<String, String> getCustomFields() { return customFields; }

    public URL getImageUrl() { return imageUrl; }

    public URL getZoomImageUrl() { return zoomImageUrl; }

    public String getCategoryPath() { return categoryPath; }

    public Boolean getAvailable() { return available; }

    public String getProductDescription() { return productDescription; }

    public Float getPrice() { return price; }

    public Float getMsrp() { return msrp; }

    public String getAlbum() { return album; }

    public String getActor() { return actor; }

    public String getArtist() { return artist; }

    public String getAuthor() { return author; }

    public String getBrand() { return brand; }

    public Integer getYear() { return year; }

}
