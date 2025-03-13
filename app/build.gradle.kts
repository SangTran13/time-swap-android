import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "timeswap.application"
    compileSdk = 35

    defaultConfig {
        applicationId = "timeswap.application"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "google_maps_api_key", project.findProperty("GOOGLE_MAPS_API_KEY")?.toString() ?: "")
/*
        buildConfigField("String", "BASE_AUTH_URL", "\"${project.properties["BASE_AUTH_URL"]}\"")
        buildConfigField("String", "BASE_API_URL", "\"${project.properties["BASE_API_URL"]}\"")
        buildConfigField("String", "BASE_GPT_URL", "\"${project.properties["BASE_GPT_URL"]}\"")
        buildConfigField("String", "KEY_GPT_API", "\"${project.properties["KEY_GPT_API"]}\"")
        buildConfigField("String", "KEY_ORGANIZATION_GPT_API", "\"${project.properties["KEY_ORGANIZATION_GPT_API"]}\"")
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${project.properties["GOOGLE_MAPS_API_KEY"]}\"")
        buildConfigField("String", "CONFIRM_EMAIL_AUTH_URL", "\"${project.properties["CONFIRM_EMAIL_AUTH_URL"]}\"")*/

        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        localPropertiesFile.inputStream().use { properties.load(it) }

        buildConfigField("String", "BASE_AUTH_URL", "\"${properties.getProperty("BASE_AUTH_URL")}\"")
        buildConfigField("String", "BASE_API_URL", "\"${properties.getProperty("BASE_API_URL")}\"")
        buildConfigField("String", "BASE_GPT_URL", "\"${properties.getProperty("BASE_GPT_URL")}\"")
        buildConfigField("String", "KEY_GPT_API", "\"${properties.getProperty("KEY_GPT_API")}\"")
        buildConfigField("String", "KEY_ORGANIZATION_GPT_API", "\"${properties.getProperty("KEY_ORGANIZATION_GPT_API")}\"")
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${properties.getProperty("GOOGLE_MAPS_API_KEY")}\"")
        buildConfigField("String", "CONFIRM_EMAIL_AUTH_URL", "\"${properties.getProperty("CONFIRM_EMAIL_AUTH_URL")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation (libs.reorderable)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}