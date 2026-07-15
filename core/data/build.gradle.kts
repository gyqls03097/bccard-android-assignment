plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "test.bccard.android.assignment.core.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
}
