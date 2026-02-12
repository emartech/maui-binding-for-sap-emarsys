package com.emarsys.maui;

import com.emarsys.maui.mapper.GeofenceMapper;
import com.emarsys.maui.model.EMSGeofence;
import com.emarsys.maui.model.EMSGeofenceTrigger;
import com.emarsys.mobileengage.api.geofence.Geofence;
import com.emarsys.mobileengage.api.geofence.Trigger;
import com.emarsys.mobileengage.api.geofence.TriggerType;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GeofenceMapperTest {

    @Test
    public void testMap_withEmptyList() {
        List<EMSGeofence> result = GeofenceMapper.map(new ArrayList<>());

        assertEquals("Result should be empty list", 0, result.size());
    }

    @Test
    public void testMap_withCompleteGeofence() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("key", "value");
        
        Map<String, Object> actionMap = new HashMap<>();
        actionMap.put("id", "testId");
        actionMap.put("type", "MECustomEvent");
        actionMap.put("name", "testName");
        actionMap.put("payload", payloadMap);
        
        JSONObject actionJson = new JSONObject(actionMap);

        Trigger trigger = new Trigger(
            "testTriggerId",
            TriggerType.ENTER,
            123,
            actionJson
        );

        Geofence geofence = new Geofence(
            "testGeofenceId",
            12.34,
            56.78,
            30.0,
            90.12,
            Arrays.asList(trigger)
        );

        List<EMSGeofence> result = GeofenceMapper.map(Arrays.asList(geofence));

        assertEquals("Result should have 1 geofence", 1, result.size());

        EMSGeofence resultGeofence = result.get(0);
        assertEquals("Geofence id should match", "testGeofenceId", resultGeofence.getId());
        assertEquals("Geofence lat should match", 12.34, resultGeofence.getLat(), 0.001);
        assertEquals("Geofence lon should match", 56.78, resultGeofence.getLon(), 0.001);
        assertEquals("Geofence radius should match", 30.0, resultGeofence.getRadius(), 0.001);
        assertEquals("Geofence waitInterval should match", 90.12, resultGeofence.getWaitInterval(), 0.001);

        List<EMSGeofenceTrigger> triggers = resultGeofence.getTriggers();
        assertNotNull("Triggers should not be null", triggers);
        assertEquals("Triggers should have 1 item", 1, triggers.size());

        EMSGeofenceTrigger resultTrigger = triggers.get(0);
        assertEquals("Trigger id should match", "testTriggerId", resultTrigger.getId());
        assertEquals("Trigger type should match", "ENTER", resultTrigger.getType());
        assertEquals("Trigger loiteringDelay should match", 123, resultTrigger.getLoiteringDelay());
        
        JSONObject resultAction = resultTrigger.getAction();
        assertNotNull("Trigger action should not be null", resultAction);
        assertSame("Trigger action should be the same instance as the input action", actionJson, resultAction);
    }

    @Test
    public void testMap_withMultipleGeofences() {
        JSONObject actionJson1 = new JSONObject();

        Trigger trigger1 = new Trigger(
            "testTriggerId1",
            TriggerType.ENTER,
            123,
            actionJson1
        );

        Geofence geofence1 = new Geofence(
            "testGeofenceId1",
            12.34,
            56.78,
            30.0,
            90.12,
            Arrays.asList(trigger1)
        );

        JSONObject actionJson2 = new JSONObject();

        Trigger trigger2 = new Trigger(
            "testTriggerId2",
            TriggerType.EXIT,
            456,
            actionJson2
        );

        Geofence geofence2 = new Geofence(
            "testGeofenceId2",
            43.21,
            87.65,
            50.0,
            21.09,
            Arrays.asList(trigger2)
        );

        List<EMSGeofence> result = GeofenceMapper.map(Arrays.asList(geofence1, geofence2));

        assertEquals("Result should have 2 geofences", 2, result.size());

        EMSGeofence firstGeofence = result.get(0);
        assertEquals("First geofence id should match", "testGeofenceId1", firstGeofence.getId());
        assertEquals("First geofence lat should match", 12.34, firstGeofence.getLat(), 0.001);
        assertEquals("First geofence lon should match", 56.78, firstGeofence.getLon(), 0.001);

        EMSGeofence secondGeofence = result.get(1);
        assertEquals("Second geofence id should match", "testGeofenceId2", secondGeofence.getId());
        assertEquals("Second geofence lat should match", 43.21, secondGeofence.getLat(), 0.001);
        assertEquals("Second geofence lon should match", 87.65, secondGeofence.getLon(), 0.001);
    }
}
