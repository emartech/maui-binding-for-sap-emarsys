//
//  Copyright © 2026 Emarsys. All rights reserved.
//

import Foundation
import EmarsysSDK

class MessageMapper {
    static func mapMessages(_ messages: [EMSMessage]) -> [[String: Any]] {
        let messageDicts: [[String: Any]] = messages.map { message in
            var messageDict = [String: Any]()
            messageDict["id"] = message.id
            messageDict["campaignId"] = message.campaignId
            messageDict["collapseId"] = message.collapseId
            messageDict["title"] = message.title
            messageDict["body"] = message.body
            messageDict["imageUrl"] = message.imageUrl
            messageDict["receivedAt"] = message.receivedAt
            messageDict["updatedAt"] = message.updatedAt
            messageDict["expiresAt"] = message.expiresAt
            messageDict["tags"] = message.tags
            messageDict["properties"] = message.properties
            if let actions = message.actions {
                messageDict["actions"] = mapActions(actions)
            }
            return messageDict
        }
        return messageDicts
    }
    
    static func mapActions(_ actions: [EMSActionModelProtocol]) -> [[String: Any]] {
        let actionDicts: [[String: Any]] = actions.map { action in
            var actionDict = [String: Any]()
            if let appEvent = action as? EMSAppEventActionModel {
                actionDict["id"] = appEvent.id
                actionDict["title"] = appEvent.title
                actionDict["type"] = appEvent.type
                actionDict["name"] = appEvent.name
                actionDict["payload"] = appEvent.payload
            } else if let customEvent = action as? EMSCustomEventActionModel {
                actionDict["id"] = customEvent.id
                actionDict["title"] = customEvent.title
                actionDict["type"] = customEvent.type
                actionDict["name"] = customEvent.name
                actionDict["payload"] = customEvent.payload
            } else if let externalUrl = action as? EMSOpenExternalUrlActionModel {
                actionDict["id"] = externalUrl.id
                actionDict["title"] = externalUrl.title
                actionDict["type"] = externalUrl.type
                actionDict["url"] = externalUrl.url.absoluteString
            }
            return actionDict
        }
        return actionDicts
    }
}
