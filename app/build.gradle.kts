plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.zackjp.mockamazon"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.zackjp.mockamazon"
        minSdk = 26
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

        // A profilable build, based on the Release variant, for performance tools & analysis
        create("benchmark") {
            val release by getting
            val debug by getting

            initWith(release)
            isDebuggable = false
            signingConfig = debug.signingConfig
            // Includes source sets from other modules without having to define their own benchmark variant
            matchingFallbacks += "release"

            proguardFiles(
                *(release.proguardFiles.toTypedArray() + file("proguard-benchmark.pro"))
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.metrics.performance)
    implementation(projects.core.analytics.api)
    implementation(projects.core.analytics.impl)
    implementation(projects.feature.cart.impl)
    implementation(projects.feature.checkout)
    implementation(projects.feature.home)
    implementation(projects.feature.product)
    implementation(projects.feature.search)
    implementation(projects.lib.shared)
    testImplementation(projects.lib.sharedTestUtils)

    val benchmarkImplementation by configurations.getting
    benchmarkImplementation(platform(libs.androidx.compose.bom))
    benchmarkImplementation(libs.androidx.compose.runtime.tracing)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.orbit.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
