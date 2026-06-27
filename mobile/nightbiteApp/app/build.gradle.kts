plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

val gdxNativesArmeabiV7a by configurations.creating
val gdxNativesArm64V8a by configurations.creating
val gdxNativesX86 by configurations.creating
val gdxNativesX86_64 by configurations.creating

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

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("src/main/jniLibs")
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["usesCleartextTraffic"] = "true"
        }

        release {
            manifestPlaceholders["usesCleartextTraffic"] = "true"
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
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation("androidx.datastore:datastore-preferences:1.1.7")

    implementation(libs.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // LibGDX
    implementation(libs.bundles.libgdx)
    implementation(libs.androidx.fragment.ktx)

    val gdxVersion = libs.versions.gdx.get()

    gdxNativesArmeabiV7a("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    gdxNativesArm64V8a("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    gdxNativesX86("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    gdxNativesX86_64("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")

    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

tasks.register<Copy>("copyAndroidNatives") {
    group = "libgdx"
    description = "Copia las librerías nativas de LibGDX para Android."

    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE

    into("src/main/jniLibs")

    from(gdxNativesArmeabiV7a.map { zipTree(it) }) {
        include("*.so")
        into("armeabi-v7a")
    }

    from(gdxNativesArm64V8a.map { zipTree(it) }) {
        include("*.so")
        into("arm64-v8a")
    }

    from(gdxNativesX86.map { zipTree(it) }) {
        include("*.so")
        into("x86")
    }

    from(gdxNativesX86_64.map { zipTree(it) }) {
        include("*.so")
        into("x86_64")
    }
}

tasks.named("preBuild") {
    dependsOn("copyAndroidNatives")
}