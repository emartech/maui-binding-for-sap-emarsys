package com.emarsys.maui;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.emarsys.Emarsys;
import com.emarsys.config.EmarsysConfig;
import com.emarsys.maui.model.EMSConfig;
import java.util.Map;

public class DotnetEmarsys {

    public static void setup(@NonNull EMSConfig dotnetConfig) {
        EmarsysConfig.Builder builder = new EmarsysConfig.Builder();
        builder.application(dotnetConfig.getApplication());
        if (dotnetConfig.getApplicationCode() != null && !dotnetConfig.getApplicationCode().isEmpty()) {
            builder.applicationCode(dotnetConfig.getApplicationCode());
        }
        if (dotnetConfig.getMerchantId() != null && !dotnetConfig.getMerchantId().isEmpty()) {
            builder.merchantId(dotnetConfig.getMerchantId());
        }
        if (dotnetConfig.getSharedPackageNames() != null && !dotnetConfig.getSharedPackageNames().isEmpty()) {
            builder.sharedPackageNames(dotnetConfig.getSharedPackageNames());
        }
        if (dotnetConfig.getSharedSecret() != null && !dotnetConfig.getSharedSecret().isEmpty()) {
            builder.sharedSecret(dotnetConfig.getSharedSecret());
        }
        if (dotnetConfig.isEnableConsoleLogging()) {
            builder.enableVerboseConsoleLogging();
        }
        EmarsysConfig config = builder.build();
        Emarsys.setup(config);
    }

    public static void setContact(int contactFieldId, @NonNull String contactFieldValue, @NonNull CompletionListener completionListener) {
        Emarsys.setContact(contactFieldId, contactFieldValue, completionListener::onCompleted);
    }

    public static void clearContact(@NonNull CompletionListener completionListener) {
        Emarsys.clearContact(completionListener::onCompleted);
    }

    public static void trackCustomEvent(@NonNull String eventName, Map<String, String> eventAttributes, @NonNull CompletionListener completionListener) {
        Emarsys.trackCustomEvent(eventName, eventAttributes, completionListener::onCompleted);
    }

    public static void trackDeepLink(Activity activity, Intent intent, @NonNull CompletionListener completionListener) {
        Emarsys.trackDeepLink(activity, intent, completionListener::onCompleted);
    }

}
