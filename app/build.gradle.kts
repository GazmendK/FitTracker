plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tracker"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment:2.8.2")
    implementation ("androidx.navigation:navigation-ui:2.8.2")
    implementation ("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.google.mlkit:barcode-scanning:17.3.0")
    implementation ("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")

    val camerax_version = "1.4.0"
     implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")

    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    // CardView
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("androidx.camera:camera-view:1.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


}