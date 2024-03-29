import org.jetbrains.kotlin.gradle.tasks.KaptGenerateStubs


allprojects {
    tasks.withType<JavaCompile> {
        options.compilerArgs.plusAssign("-jvm-target=17")
    }

    tasks.withType<KaptGenerateStubs> {
        kotlinOptions.jvmTarget = "17"
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.realmDB) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.chain.resolver) apply false
    alias(libs.plugins.android.library) apply false
}
true // Needed to make the Suppress annotation work for the plugins block