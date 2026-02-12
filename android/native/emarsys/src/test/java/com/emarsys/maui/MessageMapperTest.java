package com.emarsys.maui;

import com.emarsys.maui.mapper.MessageMapper;
import com.emarsys.mobileengage.api.action.ActionModel;
import com.emarsys.mobileengage.api.action.AppEventActionModel;
import com.emarsys.mobileengage.api.action.CustomEventActionModel;
import com.emarsys.mobileengage.api.action.OpenExternalUrlActionModel;
import com.emarsys.mobileengage.api.inbox.Message;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MessageMapperTest {

    @Test
    public void testMap_withCompleteMessage() throws MalformedURLException {
        Map<String, Object> appEventPayload = new HashMap<>();
        appEventPayload.put("k1", "v1");
        appEventPayload.put("k2", 123);
        
        ActionModel appEventAction = new AppEventActionModel(
            "actionId1",
            "actionTitle1",
            "MEAppEvent",
            "actionName1",
            appEventPayload
        );

        Map<String, Object> customEventPayload = new HashMap<>();
        customEventPayload.put("k1", "v1");
        customEventPayload.put("k2", 456);

        ActionModel customEventAction = new CustomEventActionModel(
            "actionId2",
            "actionTitle2",
            "MECustomEvent",
            "actionName2",
            customEventPayload
        );

        ActionModel urlAction = new OpenExternalUrlActionModel(
            "actionId3",
            "actionTitle3",
            "OpenExternalUrl",
            new URL("https://www.emarsys.com")
        );

        List<ActionModel> actions = Arrays.asList(appEventAction, customEventAction, urlAction);
        List<String> tags = Arrays.asList("TAG1", "TAG2", "TAG3");
        Map<String, String> properties = new HashMap<>();
        properties.put("k1", "v1");
        properties.put("k2", "v2");

        Message message = mock(Message.class);
        when(message.getId()).thenReturn("testId");
        when(message.getCampaignId()).thenReturn("testCampaignId");
        when(message.getCollapseId()).thenReturn("testCollapseId");
        when(message.getTitle()).thenReturn("testTitle");
        when(message.getBody()).thenReturn("testBody");
        when(message.getImageUrl()).thenReturn("testImageUrl");
        when(message.getReceivedAt()).thenReturn(1234L);
        when(message.getUpdatedAt()).thenReturn(4321L);
        when(message.getExpiresAt()).thenReturn(5678L);
        when(message.getTags()).thenReturn(tags);
        when(message.getProperties()).thenReturn(properties);
        when(message.getActions()).thenReturn(actions);

        List<Map<String, Object>> result = MessageMapper.map(Arrays.asList(message));

        assertEquals("Result should have 1 message", 1, result.size());

        Map<String, Object> messageDict = result.get(0);
        assertEquals("Message id should match", "testId", messageDict.get("id"));
        assertEquals("Message campaignId should match", "testCampaignId", messageDict.get("campaignId"));
        assertEquals("Message collapseId should match", "testCollapseId", messageDict.get("collapseId"));
        assertEquals("Message title should match", "testTitle", messageDict.get("title"));
        assertEquals("Message body should match", "testBody", messageDict.get("body"));
        assertEquals("Message imageUrl should match", "testImageUrl", messageDict.get("imageUrl"));
        assertEquals("Message receivedAt should match", 1234L, messageDict.get("receivedAt"));
        assertEquals("Message updatedAt should match", 4321L, messageDict.get("updatedAt"));
        assertEquals("Message expiresAt should match", 5678L, messageDict.get("expiresAt"));

        @SuppressWarnings("unchecked")
        List<String> resultTags = (List<String>) messageDict.get("tags");
        assertNotNull("Tags array should not be null", resultTags);
        assertEquals("Tags array should have 3 items", 3, resultTags.size());
        assertEquals("First tag should match", "TAG1", resultTags.get(0));
        assertEquals("Second tag should match", "TAG2", resultTags.get(1));
        assertEquals("Third tag should match", "TAG3", resultTags.get(2));

        @SuppressWarnings("unchecked")
        Map<String, String> resultProperties = (Map<String, String>) messageDict.get("properties");
        assertNotNull("Properties dictionary should not be null", resultProperties);
        assertEquals("Properties should have 2 items", 2, resultProperties.size());
        assertEquals("First property should match", "v1", resultProperties.get("k1"));
        assertEquals("Second property should match", "v2", resultProperties.get("k2"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resultActions = (List<Map<String, Object>>) messageDict.get("actions");
        assertNotNull("Actions array should not be null", resultActions);
        assertEquals("Actions array should have 3 items", 3, resultActions.size());

        Map<String, Object> firstAction = resultActions.get(0);
        assertEquals("First action id should match", "actionId1", firstAction.get("id"));
        assertEquals("First action title should match", "actionTitle1", firstAction.get("title"));
        assertEquals("First action type should match", "MEAppEvent", firstAction.get("type"));
        assertEquals("First action name should match", "actionName1", firstAction.get("name"));
        @SuppressWarnings("unchecked")
        Map<String, Object> firstActionPayload = (Map<String, Object>) firstAction.get("payload");
        assertNotNull("First action payload should not be null", firstActionPayload);
        assertEquals("First action payload k1 should match", "v1", firstActionPayload.get("k1"));
        assertEquals("First action payload k2 should match", 123, firstActionPayload.get("k2"));

        Map<String, Object> secondAction = resultActions.get(1);
        assertEquals("Second action id should match", "actionId2", secondAction.get("id"));
        assertEquals("Second action title should match", "actionTitle2", secondAction.get("title"));
        assertEquals("Second action type should match", "MECustomEvent", secondAction.get("type"));
        assertEquals("Second action name should match", "actionName2", secondAction.get("name"));

        Map<String, Object> thirdAction = resultActions.get(2);
        assertEquals("Third action id should match", "actionId3", thirdAction.get("id"));
        assertEquals("Third action title should match", "actionTitle3", thirdAction.get("title"));
        assertEquals("Third action type should match", "OpenExternalUrl", thirdAction.get("type"));
        assertEquals("Third action url should match", "https://www.emarsys.com", thirdAction.get("url"));
    }

    @Test
    public void testMapActions_withAppEventActionModel() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("k1", "v1");

        ActionModel actionModel = new AppEventActionModel(
            "testActionId",
            "testActionTitle",
            "MEAppEvent",
            "testEventName",
            payload
        );

        List<Map<String, Object>> result = MessageMapper.mapActions(Arrays.asList(actionModel));

        assertEquals("Result should have 1 action", 1, result.size());

        Map<String, Object> actionDict = result.get(0);
        assertEquals("Action id should match", "testActionId", actionDict.get("id"));
        assertEquals("Action title should match", "testActionTitle", actionDict.get("title"));
        assertEquals("Action type should match", "MEAppEvent", actionDict.get("type"));
        assertEquals("Action name should match", "testEventName", actionDict.get("name"));

        @SuppressWarnings("unchecked")
        Map<String, Object> resultPayload = (Map<String, Object>) actionDict.get("payload");
        assertNotNull("Payload should not be null", resultPayload);
        assertEquals("Payload should match", "v1", resultPayload.get("k1"));
    }

    @Test
    public void testMapActions_withCustomEventActionModel() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("k1", "v1");

        ActionModel actionModel = new CustomEventActionModel(
            "testActionId",
            "testActionTitle",
            "MECustomEvent",
            "testEventName",
            payload
        );

        List<Map<String, Object>> result = MessageMapper.mapActions(Arrays.asList(actionModel));

        assertEquals("Result should have 1 action", 1, result.size());

        Map<String, Object> actionDict = result.get(0);
        assertEquals("Action id should match", "testActionId", actionDict.get("id"));
        assertEquals("Action title should match", "testActionTitle", actionDict.get("title"));
        assertEquals("Action type should match", "MECustomEvent", actionDict.get("type"));
        assertEquals("Action name should match", "testEventName", actionDict.get("name"));

        @SuppressWarnings("unchecked")
        Map<String, Object> resultPayload = (Map<String, Object>) actionDict.get("payload");
        assertNotNull("Payload should not be null", resultPayload);
        assertEquals("Payload should match", "v1", resultPayload.get("k1"));
    }

    @Test
    public void testMapActions_withOpenExternalUrlActionModel() throws MalformedURLException {
        ActionModel actionModel = new OpenExternalUrlActionModel(
            "testActionId",
            "testActionTitle",
            "OpenExternalUrl",
            new URL("https://www.emarsys.com")
        );

        List<Map<String, Object>> result = MessageMapper.mapActions(Arrays.asList(actionModel));

        assertEquals("Result should have 1 action", 1, result.size());

        Map<String, Object> actionDict = result.get(0);
        assertEquals("Action id should match", "testActionId", actionDict.get("id"));
        assertEquals("Action title should match", "testActionTitle", actionDict.get("title"));
        assertEquals("Action type should match", "OpenExternalUrl", actionDict.get("type"));
        assertEquals("Action url should match", "https://www.emarsys.com", actionDict.get("url"));
    }

    @Test
    public void testMap_withEmptyArray() {
        List<Map<String, Object>> result = MessageMapper.map(new ArrayList<>());

        assertEquals("Result should be empty array", 0, result.size());
    }

    @Test
    public void testMapActions_withEmptyArray() {
        List<Map<String, Object>> result = MessageMapper.mapActions(new ArrayList<>());

        assertEquals("Result should be empty array", 0, result.size());
    }

    @Test
    public void testMap_withMultipleMessages() {
        List<String> tags1 = Arrays.asList("TAG1");
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("k1", "v1");

        Message message1 = mock(Message.class);
        when(message1.getId()).thenReturn("id1");
        when(message1.getCampaignId()).thenReturn("campaign1");
        when(message1.getCollapseId()).thenReturn(null);
        when(message1.getTitle()).thenReturn("title1");
        when(message1.getBody()).thenReturn("body1");
        when(message1.getImageUrl()).thenReturn(null);
        when(message1.getReceivedAt()).thenReturn(1234L);
        when(message1.getUpdatedAt()).thenReturn(4321L);
        when(message1.getExpiresAt()).thenReturn(5678L);
        when(message1.getTags()).thenReturn(tags1);
        when(message1.getProperties()).thenReturn(properties1);
        when(message1.getActions()).thenReturn(null);

        List<String> tags2 = Arrays.asList("TAG2");
        Map<String, String> properties2 = new HashMap<>();
        properties2.put("k1", "v1");

        Message message2 = mock(Message.class);
        when(message2.getId()).thenReturn("id2");
        when(message2.getCampaignId()).thenReturn("campaign2");
        when(message2.getCollapseId()).thenReturn(null);
        when(message2.getTitle()).thenReturn("title2");
        when(message2.getBody()).thenReturn("body2");
        when(message2.getImageUrl()).thenReturn(null);
        when(message2.getReceivedAt()).thenReturn(1234L);
        when(message2.getUpdatedAt()).thenReturn(4321L);
        when(message2.getExpiresAt()).thenReturn(5678L);
        when(message2.getTags()).thenReturn(tags2);
        when(message2.getProperties()).thenReturn(properties2);
        when(message2.getActions()).thenReturn(null);

        List<Map<String, Object>> result = MessageMapper.map(Arrays.asList(message1, message2));

        assertEquals("Result should have 2 messages", 2, result.size());
        assertEquals("First message id should match", "id1", result.get(0).get("id"));
        assertEquals("Second message id should match", "id2", result.get(1).get("id"));
    }
}
