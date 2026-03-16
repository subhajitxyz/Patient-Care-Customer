# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Firebase
-keep class com.google.firebase.components.ComponentRegistrar
-keep class * implements com.google.firebase.components.ComponentRegistrar
-dontwarn com.google.firebase.**
-keepattributes *Annotation*

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class hilt_aggregated_deps.** { *; }

# Kotlin metadata
-keep class kotlin.Metadata { *; }

# Models
-keep class com.real.patientcare.domain.model.** { *; }
-keep class com.real.patientcare.data.model.** { *; }