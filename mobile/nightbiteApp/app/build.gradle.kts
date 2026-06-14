import java.util.Properties

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { input ->
            load(input)
        }
    }
}

val debugBaseUrl: String =
    localProperties.getProperty("NIGHTBITE_DEBUG_BASE_URL")
        ?: "http://10.131.181.86:8080/"

val releaseBaseUrl: String =
    localProperties.getProperty("NIGHTBITE_RELEASE_BASE_URL")
        ?: "https://api-nightbite.com/"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //alias(libs.plugins.ksp)
}

android {
    namespace = "ni.edu.uam.nightbiteapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "ni.edu.uam.nightbiteapp"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"$debugBaseUrl\""
            )

            manifestPlaceholders["usesCleartextTraffic"] = "true"
        }

        release {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"$releaseBaseUrl\""
            )

            manifestPlaceholders["usesCleartextTraffic"] = "false"

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // Navigation Compose
    implementation(libs.navigation.compose)

    // ViewModel con Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Retrofit para consumir la API Spring Boot
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room para almacenamiento local
    //implementation(libs.androidx.room.runtime)
    //implementation(libs.androidx.room.ktx)
    //ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}