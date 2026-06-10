plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.zackjp.mockamazon.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    ksp(libs.hilt.compiler)

    implementation(platform(libs.androidx.compose.bom))

    implementation(project(":lib:shared"))
    implementation(project(":core:model"))
    testImplementation(project(":lib:shared-test-utils"))

    implementation(libs.hilt.android)
    implementation(libs.bundles.production.compose)
    implementation(libs.kotlinx.serialization.json)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.bundles.test.unit)
}