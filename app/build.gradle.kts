import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}




android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    namespace = "com.example.musicrecommendation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicrecommendation"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "WEATHER_API_KEY", "\"${gradleLocalProperties(rootDir, providers).getProperty("WEATHER_API_KEY")}\"")
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
}

fun gradleLocalProperties(rootDir: File): Properties {
    val properties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties
}




dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // GSON Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp (옵션)
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // All other dependencies for your app should also be here:
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.appcompat:appcompat")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    implementation(files("C:\\Users\\Jun\\Desktop\\music_recommendation_weather2\\app\\libs\\spotify-app-remote-release-0.8.0.aar"))
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.spotify.android:auth:1.2.5")
    // 필요한 추가 의존성
    implementation("androidx.browser:browser:1.7.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}

