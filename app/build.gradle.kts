plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.moviesapp"
    compileSdk = 34

    sourceSets {
        getByName("main").java.srcDirs("build/generated/source/navigation-args")
    }

    buildFeatures{
        buildConfig = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.moviesapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    //todo Deixei igual o endereço da api, mas deixei pré configurado pra sempre
    // utilizar debug para dev e release pra prod

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "API_MOVIES",
                "\"https://api.themoviedb.org\"")

            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("debug") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "API_MOVIES",
                "\"https://api.themoviedb.org\"")

            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    var retrofit_version = "2.9.0"
    var mockito_version = "4.0.0"
    var koin_version = "3.1.2"

    // JUnit
    testImplementation ("junit:junit:4.13.2")
    // Mockito
    testImplementation ("org.mockito:mockito-core:$mockito_version")
    testImplementation ("org.mockito:mockito-inline:$mockito_version")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:$mockito_version")
    // webmock
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.3")
    // FragmentTesting
    debugImplementation ("androidx.fragment:fragment-testing:1.8.0")
    // Coroutines Test
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // AndroidX Test
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    // Koin for Testing
    testImplementation("io.insert-koin:koin-test:$koin_version")
    androidTestImplementation ("io.insert-koin:koin-test:$koin_version")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Material
    implementation(libs.material)
    // Lifecycles only (without ViewModel or LiveData)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // okhttp
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.3.0")
    // kotlin based dependency injection koin
    implementation ("io.insert-koin:koin-core:$koin_version")
    implementation ("io.insert-koin:koin-android:$koin_version")
    // curl
    implementation ("com.github.mrmike:ok2curl:0.8.0")
}