plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.seuprojeto.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seuprojeto.mobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-graphics:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    
    // AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")
    
    // ThreeTenABP for Java 8 date/time API backport support
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
    
    // Fragment with ActivityResult support
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    
    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    
    // Coil para carregamento de imagens
    implementation("io.coil-kt:coil-compose:2.4.0")
    
    // Animações
    implementation("androidx.compose.animation:animation:1.5.4")
    implementation("androidx.navigation:navigation-runtime:2.7.6")

    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
}