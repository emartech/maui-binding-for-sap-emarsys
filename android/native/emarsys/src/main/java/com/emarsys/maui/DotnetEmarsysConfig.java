package com.emarsys.maui;

import android.app.Application;
import androidx.annotation.NonNull;
import com.emarsys.Emarsys;
import java.util.List;

public class DotnetEmarsysConfig {

    public static @NonNull EMSConfig build(@NonNull Application application, String applicationCode, String merchantId,
                                           List<String> sharedPackageNames, String sharedSecret, boolean enableConsoleLogging) {
        return new EMSConfig(application, applicationCode, merchantId, sharedPackageNames, sharedSecret, enableConsoleLogging);
    }

    public static void changeApplicationCode(String applicationCode, @NonNull CompletionListener completionListener) {
        Emarsys.getConfig().changeApplicationCode(applicationCode, completionListener::onCompleted);
    }

    public static void changeMerchantId(String merchantId, @NonNull CompletionListener completionListener) {
        Emarsys.getConfig().changeMerchantId(merchantId);
        completionListener.onCompleted(null);
    }

    public static String getApplicationCode() {
        return Emarsys.getConfig().getApplicationCode();
    }

    public static String getMerchantId() {
        return Emarsys.getConfig().getMerchantId();
    }

    public static @NonNull String getClientId() {
        return Emarsys.getConfig().getClientId();
    }

    public static @NonNull String getLanguageCode() {
        return Emarsys.getConfig().getLanguageCode();
    }

    public static @NonNull String getSdkVersion() {
        return Emarsys.getConfig().getSdkVersion();
    }

    public static Integer getContactFieldId() {
        return Emarsys.getConfig().getContactFieldId();
    }

}
