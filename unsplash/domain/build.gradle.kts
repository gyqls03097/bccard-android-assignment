plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "test.bccard.android.assignment.domain"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:domain"))

    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
}
