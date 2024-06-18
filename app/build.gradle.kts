plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "github.kutouzi.actassistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "github.kutouzi.actassistant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
    }
    signingConfigs {
        create("release") {
            keyAlias = "cnkey"
            keyPassword = "112450"
            storeFile = file("D:\\another\\秘钥\\cnkey.jks")
            storePassword = "112450"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation("com.github.1079374315:GSLS_Tool:v1.4.5.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta03")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}