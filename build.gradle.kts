plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.8" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false
}
