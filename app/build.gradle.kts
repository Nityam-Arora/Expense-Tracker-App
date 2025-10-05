plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.expensetrackerapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.expensetrackerapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.google.android.ump:user-messaging-platform:2.1.0")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.15.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    implementation(libs.viewpager2)
    implementation(libs.material.v1110)
    implementation(libs.gms.play.services.ads)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}