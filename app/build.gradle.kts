plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("io.realm.kotlin")
}

android {
    namespace = "yc.dev.newsapi"
    compileSdk = 34

    defaultConfig {
        applicationId = "yc.dev.newsapi"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "NEWS_API_KEY", "${properties["NEWS_API_KEY"]}")
    }

    buildTypes {
        release {
            android.buildFeatures.buildConfig = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.android.material:material:1.10.0")

    // compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.ui:ui-viewbinding:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // compose runtime
    val composeRuntimeVersion = "1.5.4"
    implementation("androidx.compose.runtime:runtime:$composeRuntimeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeRuntimeVersion")

    // Fragment delegate
    implementation("com.github.Zhuinden:fragmentviewbindingdelegate-kt:1.0.2")

    // compose navigation
    val navVersion = "2.7.5"
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")

    // Hilt
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Realm
    implementation("io.realm.kotlin:library-base:1.11.0")

    // Test libs
    // JUnit
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Kotlin test
    testImplementation(kotlin("test"))

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // Mockk
    testImplementation("io.mockk:mockk:1.13.8")

    // Mock Web Server
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    // Debug libs
    // Compose
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}