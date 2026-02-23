package com.emarsys.maui.model;

import androidx.annotation.NonNull;
import org.json.JSONObject;

public class EMSGeofenceTrigger {

    private final String id;
    private final String type;
    private final int loiteringDelay;
    private final JSONObject action;

    public EMSGeofenceTrigger(@NonNull String id, @NonNull String type, int loiteringDelay, @NonNull JSONObject action) {
        this.id = id;
        this.type = type;
        this.loiteringDelay = loiteringDelay;
        this.action = action;
    }

    public @NonNull String getId() { return id; }

    public @NonNull String getType() { return type; }

    public int getLoiteringDelay() { return loiteringDelay; }

    public @NonNull JSONObject getAction() { return action; }

}
