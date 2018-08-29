@file:Suppress("ClassName", "unused")

object Versions {
    // android
    const val compileSdk = 28
    const val minSdk = 16
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "1.0"

    const val androidGradlePlugin = "3.1.4"

    const val autoCommon = "0.10"
    const val autoService = "1.0-rc4"

    const val kotlin = "1.2.51"
    const val kotlinPoet = "0.7.0"

    const val mavenGradlePlugin = "2.1"
    const val support = "28.0.0-alpha3"
    const val traveler = "1eca65e728"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val autoCommon = "com.google.auto:auto-common:${Versions.autoCommon}"

    const val autoService = "com.google.auto.service:auto-service:${Versions.autoService}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jre7:${Versions.kotlin}"
    const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

    const val supportAppCompat = "com.android.support:appcompat-v7:${Versions.support}"

    const val traveler = "com.github.IVIanuu.Traveler:traveler:${Versions.traveler}"
    const val travelerFragments = "com.github.IVIanuu.Traveler:traveler-fragments:${Versions.traveler}"
}