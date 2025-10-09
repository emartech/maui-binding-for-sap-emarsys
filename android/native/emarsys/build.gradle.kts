plugins {
    id("com.android.library")
}

android {
    namespace = "com.emarsys.maui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Create configuration for copyDependencies
configurations {
    create("copyDependencies")
}

dependencies {
    // Add package dependency for binding library
    implementation("com.emarsys:emarsys-sdk:3.10.2")
    implementation("com.emarsys:emarsys-firebase:3.10.2")

    // Copy dependencies for binding library
    "copyDependencies"("com.emarsys:emarsys-sdk:3.10.2")
    "copyDependencies"("com.emarsys:emarsys-firebase:3.10.2")
}

// Copy dependencies for binding library
project.afterEvaluate {
    tasks.register<Copy>("copyDeps") {
        from(configurations["copyDependencies"])
        into("${buildDir}/outputs/deps")
    }
    tasks.named("preBuild") { finalizedBy("copyDeps") }
}
