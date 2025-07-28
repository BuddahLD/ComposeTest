plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.gmail.danylo.oliinyk.composetest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gmail.danylo.oliinyk.composetest"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        renderscriptTargetApi = 19
        renderscriptSupportModeEnabled = true
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(platform(libs.compose.bom.beta))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.lifecycle.viewmodel)
    implementation(libs.compose.activity)

    // Other
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.timber)
    implementation(libs.webrtc.sdk)
}
