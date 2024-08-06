import org.gradle.internal.classpath.Instrumented.systemProperty

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.esnanta.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.esnanta.storyapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "TOKEN_GITHUB", "\"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\"")
        buildConfigField("String", "BASE_URL_API", "\"https://story-api.dicoding.dev/v1/\"")
        buildConfigField("String", "DATABASE_NAME", "\"story_appx.db\"")
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        unitTests.all {
            systemProperty("net.bytebuddy.experimental", "true")
        }
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.datastore.preferences)

    // ui and animation splash and
    implementation(libs.lottie)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.paging.runtime.ktx)

    // liveData, viewModel, coroutines dependencies.
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room dependency
    ksp(libs.room.compiler)
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.paging)
    testImplementation (libs.androidx.room.testing)

    // glide dependencies
    implementation(libs.glide)
    ksp(libs.ksp)

    // retrofit dependencies
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation (libs.okhttp3.logging.interceptor)

    // map dependencies
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    //desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // For Android instrumented tests
    androidTestImplementation (libs.androidx.core.testing)
    androidTestImplementation (libs.kotlinx.coroutines.test)
    // For local unit tests
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.androidx.paging.common)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)
}