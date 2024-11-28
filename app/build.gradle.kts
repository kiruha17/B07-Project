plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.b07group57"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.b07group57"
        minSdk = 24
        targetSdk = 34
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        emulatorControl {
            enable = true
        }
    }
}

dependencies {
    implementation(libs.jbcrypt)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    // implementation(libs.fragment.testing)
    implementation(libs.fragment.testing)
    // implementation(libs.fragment.testing)
    // implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.database)

    // Chart and graph library
    implementation(libs.cardview)
    implementation(libs.eazegraph)
    implementation(libs.library)
    implementation(libs.firebase.database.v2053)

    implementation (libs.espresso.core.v361)
    implementation (libs.androidx.junit.v121)
    implementation (libs.core)
    implementation (libs.rules)
    implementation (libs.androidx.core)
    implementation(libs.firebase.database.v2033)
}
