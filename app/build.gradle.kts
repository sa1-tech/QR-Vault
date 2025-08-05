plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.qrvault"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.qrvault"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
//    implementation(libs.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // QR Code
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.5.2")

    // Firebase SDKs
    implementation("com.google.firebase:firebase-analytics:21.6.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.6.1")


    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    // Multidex (optional)
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
}
