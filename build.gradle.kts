plugins {
    alias(libs.plugins.android.application) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.10.2" apply false

    // Stage 3: KSP plugin
    id("com.google.devtools.ksp") version "2.3.6" apply false
}