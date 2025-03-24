
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")

}

android {
    namespace = "fr.isen.RAVAN.isensmartcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.isen.RAVAN.isensmartcompanion"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Récupérer la clé API depuis local.properties
            val localProperties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }
            val apiKey = localProperties.getProperty("API_KEY") ?: ""
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Récupérer la clé API depuis local.properties
            val localProperties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }
            val apiKey = localProperties.getProperty("API_KEY") ?: ""
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }
    }

    buildFeatures {
        buildConfig = true
        privacySandbox {
            enable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // SDK Google AI Client
    implementation("com.google.ai.client.generativeai:generativeai:0.4.4")
    // implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.privacysandbox.ads:ads-adservices:1.1.0-beta12")
    implementation("androidx.privacysandbox.sdkruntime:sdkruntime-core:1.0.0-alpha16")
    implementation("androidx.privacysandbox.tools:tools-core:1.0.0-alpha12")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.20")
    implementation(libs.cronet.embedded)
    implementation(libs.common)
    implementation(libs.generativeai)
    implementation(libs.firebase.vertexai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}