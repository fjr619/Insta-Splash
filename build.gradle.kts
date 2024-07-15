// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotin.plugin.compose) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.mapsplatform.secrets) apply false
    alias(libs.plugins.room) apply false
}