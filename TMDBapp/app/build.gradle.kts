import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

android {
    namespace = "com.example.tmdbapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tmdbapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { inputStream ->
                localProperties.load(inputStream)
            }
        }
        val apiKey = localProperties.getProperty("API_KEY")
        buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    // ViewModel for Compose
    implementation(libs.lifecycle.viewmodel.compose)

    // Navigation for Compose
    implementation(libs.androidx.navigation.compose)

    // Retrofit for Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Gson converter
    implementation(libs.logging.interceptor) // For logging API requests (optional)

    // Coil for Image Loading
    implementation(libs.coil.compose)

    // Koin for Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Kotlin coroutines test
    testImplementation(libs.kotlinx.coroutines.test)

    // Kotlin test
    testImplementation(kotlin("test-junit5"))

    // Junit5
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Turbine for coroutine and flow tests
    testImplementation(libs.turbine)

    // Mockk
    testImplementation(libs.mockk.mockk)
    // Mockk for unit testing
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    // Mockk for instrumented testing
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)

}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    testLogging {
        events("passed", "skipped", "failed")
    }
}