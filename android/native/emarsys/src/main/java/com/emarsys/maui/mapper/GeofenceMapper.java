package com.emarsys.maui.mapper;

import androidx.annotation.NonNull;
import com.emarsys.maui.model.EMSGeofence;
import com.emarsys.maui.model.EMSGeofenceTrigger;
import com.emarsys.mobileengage.api.geofence.Geofence;
import com.emarsys.mobileengage.api.geofence.Trigger;
import java.util.ArrayList;
import java.util.List;

public class GeofenceMapper {

    public static @NonNull List<EMSGeofence> map(@NonNull List<Geofence> geofences) {
        List<EMSGeofence> _geofences = new ArrayList<>();
        for (int i = 0; i < geofences.size(); i++) {
            Geofence g = geofences.get(i);
            List<EMSGeofenceTrigger> triggers = new ArrayList<>();
            for (int j = 0; j < g.getTriggers().size(); j++) {
                Trigger t = g.getTriggers().get(j);
                triggers.add(new EMSGeofenceTrigger(t.getId(), t.getType().toString(), t.getLoiteringDelay(), t.getAction()));
            }
            _geofences.add(new EMSGeofence(g.getId(), g.getLat(), g.getLon(), g.getRadius(), g.getWaitInterval(), triggers));
        }
        return _geofences;
    }

}
