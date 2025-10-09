//
//  Copyright © 2024 Emarsys. All rights reserved.
//

import Foundation
import EmarsysSDK

@objc(DotnetEmarsysConfig)
public class DotnetEmarsysConfig: NSObject {
    
    @objc
    public static func build(_ applicationCode: String?, _ merchantId: String?,
                             _ sharedKeychainAccessGroup: String?, _ enableConsoleLogging: Bool = false) -> EMSConfig {
        let config = EMSConfig.make { (build) in
            if let applicationCode = applicationCode, applicationCode != "" {
                build.setMobileEngageApplicationCode(applicationCode)
            }
            if let merchantId = merchantId, merchantId != "" {
                build.setMerchantId(merchantId)
            }
            if let sharedKeychainAccessGroup = sharedKeychainAccessGroup, sharedKeychainAccessGroup != "" {
                build.setSharedKeychainAccessGroup(sharedKeychainAccessGroup)
            }
            if enableConsoleLogging {
                build.enableConsoleLogLevels([EMSLogLevel.basic, EMSLogLevel.error, EMSLogLevel.info, EMSLogLevel.debug])
            }
        }
        return config
    }
    
    @objc
    public static func changeApplicationCode(_ applicationCode: String?, _ completionBlock: @escaping CompletionBlock) {
        Emarsys.config.changeApplicationCode(applicationCode: applicationCode, completionBlock: completionBlock)
    }
    
    @objc
    public static func changeMerchantId(_ merchantId: String?, _ completionBlock: @escaping CompletionBlock) {
        Emarsys.config.changeMerchantId(merchantId: merchantId, completionBlock: completionBlock)
    }
    
    @objc
    public static func getApplicationCode() -> String? {
        return Emarsys.config.applicationCode()
    }
    
    @objc
    public static func getMerchantId() -> String? {
        return Emarsys.config.merchantId()
    }
    
    @objc
    public static func getClientId() -> String {
        return Emarsys.config.clientId()
    }
    
    @objc
    public static func getLanguageCode() -> String {
        return Emarsys.config.languageCode()
    }
    
    @objc
    public static func getSdkVersion() -> String {
        return Emarsys.config.sdkVersion()
    }
    
    @objc
    public static func getContactFieldId() -> NSNumber? {
        return Emarsys.config.contactFieldId()
    }
    
}
