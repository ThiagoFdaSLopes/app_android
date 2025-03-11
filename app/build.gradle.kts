plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Implementação para banco de dados
    id("kotlin-kapt")
}

android {
    namespace = "com.grupo.appandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.grupo.appandroid"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)
    // Implementação do navigation
    implementation(libs.androidx.navigation.compose)
    // Implementação banco de dados
    // Dependência principal do Room
    implementation(libs.androidx.room.runtime)
    // Dependência para gerar código (processamento de anotações)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)


    // Dependencia LiveData
    implementation(libs.androidx.runtime.livedata)

    // Retrofit e dependências relacionadas
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson) // Adicione esta linha
}