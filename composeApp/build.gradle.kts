plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.22"
}

kotlin {
    androidTarget()

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.adaptive.android)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.constraintlayout)
            implementation(libs.navigation.compose)
            implementation(compose.materialIconsExtended)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.kotlinx.io.core)
            
            implementation(project(":utils"))
        }

        iosMain.dependencies {
            implementation(libs.jetbrains.kotlinx.io.bytestring)
            implementation(libs.jetbrains.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "ru.fromchat.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.fromchat.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.resources {
    publicResClass = false
    packageOfResClass = "ru.fromchat.app"
    generateResClass = auto
}

tasks.register("generateResourceAccessors") {
    dependsOn(
        *(
            tasks.filter {
                it.name.startsWith("generateResourceAccessors") &&
                !it.name.matches("^(:${project.name})?generateResourceAccessors$".toRegex())
            }.toTypedArray()
        )
    )
}