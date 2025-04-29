plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.dicodingevent"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dicodingevent"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // ui
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.activity)
    implementation(libs.glide)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.legacy.support.v4)

    // dagger
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.work.runtime.ktx)

    // testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // coroutine support
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.ktx)

    // datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // workmanager
    implementation(libs.androidx.work.runtime)
    implementation(libs.android.async.http)

    // moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}

// Allow references to generate code
kapt {
    correctErrorTypes = true
}