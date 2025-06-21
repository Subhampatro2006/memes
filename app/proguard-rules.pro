# ------------------------------
# General Android settings
# ------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep all activities, services, broadcast receivers, and content providers
-keep class * extends android.app.Activity
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider

# ------------------------------
# BiometricPrompt
# ------------------------------
-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

# ------------------------------
# Room Database
# ------------------------------
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# For Room DAO and entities
-keep class com.example.memes.database.** { *; }

# ------------------------------
# Glide (optional)
# ------------------------------
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

# ------------------------------
# Java 8+ API (if using desugar)
# ------------------------------
-dontwarn java.time.**
-dontwarn java.lang.invoke.*

# ------------------------------
# Serializable models (optional)
# ------------------------------
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ------------------------------
# Debugging (optional)
# ------------------------------
# Uncomment to keep logs and debugging symbols
# -keep class android.util.Log { *; }
# -keep class * { public private *; }

# ------------------------------
# Final optimization
# ------------------------------
-dontwarn org.jetbrains.annotations.**
