package com.emarsys.maui.model;

import androidx.annotation.NonNull;
import java.util.List;

public class EMSGeofence {

    private final String id;
    private final Double lat;
    private final Double lon;
    private final Double radius;
    private final Double waitInterval;
    private final List<EMSGeofenceTrigger> triggers;

    public EMSGeofence(@NonNull String id, @NonNull Double lat, @NonNull Double lon, @NonNull Double radius,
                       Double waitInterval, @NonNull List<EMSGeofenceTrigger> triggers) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.waitInterval = waitInterval;
        this.triggers = triggers;
    }

    public @NonNull String getId() { return id; }

    public @NonNull Double getLat() { return lat; }

    public @NonNull Double getLon() { return lon; }

    public @NonNull Double getRadius() { return radius; }

    public Double getWaitInterval() { return waitInterval; }

    public @NonNull List<EMSGeofenceTrigger> getTriggers() { return triggers; }

}
