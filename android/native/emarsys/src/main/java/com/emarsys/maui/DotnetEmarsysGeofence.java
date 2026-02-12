package com.emarsys.maui;

import androidx.annotation.NonNull;
import com.emarsys.Emarsys;
import com.emarsys.maui.mapper.GeofenceMapper;
import com.emarsys.maui.model.EMSGeofence;
import com.emarsys.maui.model.EMSGeofenceTrigger;
import com.emarsys.mobileengage.api.geofence.Geofence;
import com.emarsys.mobileengage.api.geofence.Trigger;
import java.util.ArrayList;
import java.util.List;

public class DotnetEmarsysGeofence {

    public static void setInitialEnterTriggerEnabled(boolean initialEnterTriggerEnabled) {
        Emarsys.getGeofence().setInitialEnterTriggerEnabled(initialEnterTriggerEnabled);
    }

    public static void enable(@NonNull CompletionListener completionListener) {
        Emarsys.getGeofence().enable(completionListener::onCompleted);
    }

    public static void disable() {
        Emarsys.getGeofence().disable();
    }

    public static boolean isEnabled() {
        return Emarsys.getGeofence().isEnabled();
    }

    public static void setEventHandler(@NonNull EventHandler eventHandler) {
        Emarsys.getGeofence().setEventHandler(eventHandler::handleEvent);
    }

    public static @NonNull List<EMSGeofence> getRegisteredGeofences() {
        List<Geofence> geofences = Emarsys.getGeofence().getRegisteredGeofences();
        return GeofenceMapper.mapGeofences(geofences);
    }

}
