plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "ir.mahdiparastesh.pinsaver"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "0.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$project.rootDir/tools/proguard-rules-debug.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures { viewBinding = true }
}

dependencies {
    val glideVersion = "4.13.2"

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")
    implementation("com.google.android.gms:play-services-ads:20.6.0")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
}
