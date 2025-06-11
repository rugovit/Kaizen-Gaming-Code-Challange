// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
// Force any reference to com.intellij:annotations:12.0 → org.jetbrains:annotations:23.0.0
allprojects {
    configurations.all {
        resolutionStrategy {
            dependencySubstitution {
                substitute(module("com.intellij:annotations:12.0"))
                    .using(module("org.jetbrains:annotations:23.0.0"))
            }
        }
    }
}
