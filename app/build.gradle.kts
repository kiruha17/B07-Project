plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.b07group57"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.b07group57"
        minSdk = 26
        targetSdk = 35
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

//    testOptions {
//        emulatorControl {
//            enable = true
//        }
//    }
}

dependencies {
    configurations.all {
        resolutionStrategy {
            force("androidx.test:runner:1.6.1")
        }
    }

    implementation(libs.jbcrypt)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.database)

    // Chart and graph library
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.blackfizz:eazegraph:1.2.2@aar")
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("com.google.firebase:firebase-database:20.5.3")

    implementation (libs.espresso.core.v361)
    implementation (libs.androidx.junit.v121)
    implementation (libs.core)
    implementation (libs.rules)
    implementation (libs.androidx.core)
    implementation(libs.firebase.database.v2033)
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.rules.v150)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.espresso.core.v361)
}
