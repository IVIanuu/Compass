@file:Suppress("ClassName", "unused")

object Versions {
    // android
    const val compileSdk = 28
    const val minSdk = 16
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "0.0.1"

    const val androidGradlePlugin = "3.2.0"

    const val androidx = "1.0.0"

    const val autoCommon = "0.10"
    const val autoService = "1.0-rc4"

    const val kotlin = "1.3.0-rc-57"
    const val kotlinPoet = "0.7.0"

    const val mavenGradlePlugin = "2.1"
    const val traveler = "277980ee1a"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidx}"

    const val autoCommon = "com.google.auto:auto-common:${Versions.autoCommon}"

    const val autoService = "com.google.auto.service:auto-service:${Versions.autoService}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

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