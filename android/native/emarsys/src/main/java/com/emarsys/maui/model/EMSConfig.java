package com.emarsys.maui.model;

import android.app.Application;
import androidx.annotation.NonNull;
import java.util.List;

public class EMSConfig {

    private final Application application;
    private final String applicationCode;
    private final String merchantId;
    private final List<String> sharedPackageNames;
    private final String sharedSecret;
    private final boolean enableConsoleLogging;

    public EMSConfig(@NonNull Application application, String applicationCode, String merchantId,
                     List<String> sharedPackageNames, String sharedSecret, boolean enableConsoleLogging) {
        this.application = application;
        this.applicationCode = applicationCode;
        this.merchantId = merchantId;
        this.sharedPackageNames = sharedPackageNames;
        this.sharedSecret = sharedSecret;
        this.enableConsoleLogging = enableConsoleLogging;
    }

    public Application getApplication() {
        return application;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public List<String> getSharedPackageNames() {
        return sharedPackageNames;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public boolean isEnableConsoleLogging() {
        return enableConsoleLogging;
    }

}
