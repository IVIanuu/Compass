@file:Suppress("ClassName", "unused")

object Build {
    const val applicationId = "com.ivianuu.compass.sample"
    const val buildToolsVersion = "28.0.3"

    const val compileSdk = 28
    const val minSdk = 16
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "0.0.1"
}

object Versions {
    const val androidGradlePlugin = "3.2.1"

    const val androidx = "1.0.0"

    const val kotlin = "1.3.0"

    const val mavenGradlePlugin = "2.1"

    const val processingX = "a286c8ac5f"

    const val traveler = "788a0f325c"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidx}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

    const val processingX = "com.github.IVIanuu:processingx:${Versions.processingX}"

    const val traveler = "com.github.IVIanuu.traveler:traveler:${Versions.traveler}"
    const val travelerAndroid = "com.github.IVIanuu.traveler:traveler-android:${Versions.traveler}"
    const val travelerDirector =
        "com.github.IVIanuu.traveler:traveler-director:${Versions.traveler}"
    const val travelerFragment =
        "com.github.IVIanuu.traveler:traveler-fragment:${Versions.traveler}"
    const val travelerLifecycle =
        "com.github.IVIanuu.traveler:traveler-lifecycle:${Versions.traveler}"
    const val travelerPlugin =
        "com.github.IVIanuu.traveler:traveler-plugin:${Versions.traveler}"
}