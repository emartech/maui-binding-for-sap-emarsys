//
//  Copyright © 2026 Emarsys. All rights reserved.
//

import XCTest
import EmarsysSDK
@testable import MauiEmarsys

class MessageMapperTests: XCTestCase {
    
    func testMapMessages_withCompleteMessage() {
        let appEventAction = EMSAppEventActionModel(
            id: "actionId1",
            title: "actionTitle1",
            type: "MEAppEvent",
            name: "actionName1",
            payload: ["k1": "v1", "k2": 123]
        )
        
        let customEventAction = EMSCustomEventActionModel(
            id: "actionId2",
            title: "actionTitle2",
            type: "MECustomEvent",
            name: "actionName2",
            payload: ["k1": "v1", "k2": 456]
        )
        
        let urlAction = EMSOpenExternalUrlActionModel(
            id: "actionId3",
            title: "actionTitle3",
            type: "OpenExternalUrl",
            url: URL(string: "https://www.emarsys.com")!
        )

        let message = EMSMessage(
            id: "testId",
            campaignId: "testCampaignId",
            collapseId: "testCollapseId",
            title: "testTitle",
            body: "testBody",
            imageUrl: "testImageUrl",
            imageAltText: "testImageAltText",
            receivedAt: 1234,
            updatedAt: 4321,
            expiresAt: 5678,
            tags: ["TAG1", "TAG2", "TAG3"],
            properties: ["k1": "v1", "k2": "v2"],
            actions: [appEventAction, customEventAction, urlAction]
        )

        let result = MessageMapper.mapMessages([message])

        XCTAssertEqual(result.count, 1, "Result should have 1 message")
        
        let messageDict = result[0]
        XCTAssertEqual(messageDict["id"] as? String, "testId", "Message id should match")
        XCTAssertEqual(messageDict["campaignId"] as? String, "testCampaignId", "Message campaignId should match")
        XCTAssertEqual(messageDict["collapseId"] as? String, "testCollapseId", "Message collapseId should match")
        XCTAssertEqual(messageDict["title"] as? String, "testTitle", "Message title should match")
        XCTAssertEqual(messageDict["body"] as? String, "testBody", "Message body should match")
        XCTAssertEqual(messageDict["imageUrl"] as? String, "testImageUrl", "Message imageUrl should match")
        XCTAssertEqual(messageDict["receivedAt"] as? NSNumber, 1234, "Message receivedAt should match")
        XCTAssertEqual(messageDict["updatedAt"] as? NSNumber, 4321, "Message updatedAt should match")
        XCTAssertEqual(messageDict["expiresAt"] as? NSNumber, 5678, "Message expiresAt should match")

        let tags = messageDict["tags"] as? [String]
        XCTAssertNotNil(tags, "Tags array should not be nil")
        XCTAssertEqual(tags?.count, 3, "Tags array should have 3 items")
        XCTAssertEqual(tags?[0], "TAG1", "First tag should match")
        XCTAssertEqual(tags?[1], "TAG2", "Second tag should match")
        XCTAssertEqual(tags?[2], "TAG3", "Third tag should match")

        let properties = messageDict["properties"] as? [String: String]
        XCTAssertNotNil(properties, "Properties dictionary should not be nil")
        XCTAssertEqual(properties?.count, 2, "Properties should have 2 items")
        XCTAssertEqual(properties?["k1"], "v1", "First property should match")
        XCTAssertEqual(properties?["k2"], "v2", "Second property should match")

        let actions = messageDict["actions"] as? [[String: Any]]
        XCTAssertNotNil(actions, "Actions array should not be nil")
        XCTAssertEqual(actions?.count, 3, "Actions array should have 3 items")

        let firstAction = actions?[0]
        XCTAssertEqual(firstAction?["id"] as? String, "actionId1", "First action id should match")
        XCTAssertEqual(firstAction?["title"] as? String, "actionTitle1", "First action title should match")
        XCTAssertEqual(firstAction?["type"] as? String, "MEAppEvent", "First action type should match")
        XCTAssertEqual(firstAction?["name"] as? String, "actionName1", "First action name should match")
        let firstActionPayload = firstAction?["payload"] as? [String: Any]
        XCTAssertNotNil(firstActionPayload, "First action payload should not be nil")
        XCTAssertEqual(firstActionPayload?["k1"] as? String, "v1", "First action payload key11 should match")
        XCTAssertEqual(firstActionPayload?["k2"] as? Int, 123, "First action payload key12 should match")

        let secondAction = actions?[1]
        XCTAssertEqual(secondAction?["id"] as? String, "actionId2", "Second action id should match")
        XCTAssertEqual(secondAction?["title"] as? String, "actionTitle2", "Second action title should match")
        XCTAssertEqual(secondAction?["type"] as? String, "MECustomEvent", "Second action type should match")
        XCTAssertEqual(secondAction?["name"] as? String, "actionName2", "Second action name should match")

        let thirdAction = actions?[2]
        XCTAssertEqual(thirdAction?["id"] as? String, "actionId3", "Third action id should match")
        XCTAssertEqual(thirdAction?["title"] as? String, "actionTitle3", "Third action title should match")
        XCTAssertEqual(thirdAction?["type"] as? String, "OpenExternalUrl", "Third action type should match")
        XCTAssertEqual(thirdAction?["url"] as? String, "https://www.emarsys.com", "Third action url should match")
    }
    
    func testMapActions_withAppEventActionModel() {
        let actionModel = EMSAppEventActionModel(
            id: "testActionId",
            title: "testActionTitle",
            type: "MEAppEvent",
            name: "testEventName",
            payload: ["k1": "v1"]
        )
        
        let result = MessageMapper.mapActions([actionModel])
        
        XCTAssertEqual(result.count, 1, "Result should have 1 action")
        
        let actionDict = result[0]
        XCTAssertEqual(actionDict["id"] as? String, "testActionId", "Action id should match")
        XCTAssertEqual(actionDict["title"] as? String, "testActionTitle", "Action title should match")
        XCTAssertEqual(actionDict["type"] as? String, "MEAppEvent", "Action type should match")
        XCTAssertEqual(actionDict["name"] as? String, "testEventName", "Action name should match")
        
        let payload = actionDict["payload"] as? [String: String]
        XCTAssertNotNil(payload, "Payload should not be nil")
        XCTAssertEqual(payload?["k1"], "v1", "Payload should match")
    }
    
    func testMapActions_withCustomEventActionModel() {
        let actionModel = EMSCustomEventActionModel(
            id: "testActionId",
            title: "testActionTitle",
            type: "MECustomEvent",
            name: "testEventName",
            payload: ["k1": "v1"]
        )
        
        let result = MessageMapper.mapActions([actionModel])
        
        XCTAssertEqual(result.count, 1, "Result should have 1 action")
        
        let actionDict = result[0]
        XCTAssertEqual(actionDict["id"] as? String, "testActionId", "Action id should match")
        XCTAssertEqual(actionDict["title"] as? String, "testActionTitle", "Action title should match")
        XCTAssertEqual(actionDict["type"] as? String, "MECustomEvent", "Action type should match")
        XCTAssertEqual(actionDict["name"] as? String, "testEventName", "Action name should match")
        
        let payload = actionDict["payload"] as? [String: String]
        XCTAssertNotNil(payload, "Payload should not be nil")
        XCTAssertEqual(payload?["k1"], "v1", "Payload should match")
    }
    
    func testMapActions_withOpenExternalUrlActionModel() {
        let actionModel = EMSOpenExternalUrlActionModel(
            id: "testActionId",
            title: "testActionTitle",
            type: "OpenExternalUrl",
            url: URL(string: "https://www.emarsys.com")!
        )
        
        let result = MessageMapper.mapActions([actionModel])
        
        XCTAssertEqual(result.count, 1, "Result should have 1 action")
        
        let actionDict = result[0]
        XCTAssertEqual(actionDict["id"] as? String, "testActionId", "Action id should match")
        XCTAssertEqual(actionDict["title"] as? String, "testActionTitle", "Action title should match")
        XCTAssertEqual(actionDict["type"] as? String, "OpenExternalUrl", "Action type should match")
        XCTAssertEqual(actionDict["url"] as? String, "https://www.emarsys.com", "Action url should match")
    }
    
    func testMapMessages_withEmptyArray() {
        let result = MessageMapper.mapMessages([])
        XCTAssertEqual(result.count, 0, "Result should be empty array")
    }
    
    func testMapActions_withEmptyArray() {
        let result = MessageMapper.mapActions([])
        XCTAssertEqual(result.count, 0, "Result should be empty array")
    }
    
    func testMapMessages_withMultipleMessages() {
        let message1 = EMSMessage(
            id: "id1",
            campaignId: "campaign1",
            collapseId: nil,
            title: "title1",
            body: "body1",
            imageUrl: nil,
            imageAltText: nil,
            receivedAt: 1234,
            updatedAt: 4321,
            expiresAt: 5678,
            tags: ["TAG1"],
            properties: ["k1": "v1"],
            actions: nil
        )
        
        let message2 = EMSMessage(
            id: "id2",
            campaignId: "campaign2",
            collapseId: nil,
            title: "title2",
            body: "body2",
            imageUrl: nil,
            imageAltText: nil,
            receivedAt: 1234,
            updatedAt: 4321,
            expiresAt: 5678,
            tags: ["TAG2"],
            properties: ["k1": "v1"],
            actions: nil
        )
        
        let result = MessageMapper.mapMessages([message1, message2])
        
        XCTAssertEqual(result.count, 2, "Result should have 2 messages")
        XCTAssertEqual(result[0]["id"] as? String, "id1", "First message id should match")
        XCTAssertEqual(result[1]["id"] as? String, "id2", "Second message id should match")
    }
}
