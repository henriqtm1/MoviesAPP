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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            //TODO API PRO TESTE IFOOD: DEV E PROD IGUAL A URL
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

            //TODO API PRO TESTE IFOOD: DEV E PROD IGUAL A URL
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

    // JUnit
    testImplementation ("junit:junit:4.13.2")

    // Mockito
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito:mockito-inline:4.0.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // Coroutines Test
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // AndroidX Test
    testImplementation ("androidx.arch.core:core-testing:2.1.0")

    // Robolectric
    testImplementation ("org.robolectric:robolectric:4.6.1")

    var retrofit_version = "2.6.1"
    implementation("androidx.navigation:navigation-runtime-ktx:2.2.1")
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycles only (without ViewModel or LiveData)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // okhttp
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.3.0")

    // kotlin based dependency injection koin
    implementation ("io.insert-koin:koin-core:3.3.2")
    implementation ("io.insert-koin:koin-android:3.3.2")
}