plugins {
    id("com.android.application")

}

android {
    namespace = "com.devst.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.devst.app"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true // si estás usando ViewBinding
    }
}

dependencies {
    implementation(fileTree("libs"){ include ("*.aar")})
    implementation("androidx.core:core-ktx:1.12.0") // puedes usar core si quieres, aunque es KTX
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
