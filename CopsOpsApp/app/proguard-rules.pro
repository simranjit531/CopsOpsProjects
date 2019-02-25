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
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
 -keep class com.quickblox.core.account.model.** { *; }

-dontwarn org.apache.http.**
-ignorewarnings
-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatcreatorHoneycombMR2
-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.context, android.util.Attributeset);
}
##---------------End: proguard configuration for Gson  ----------
#quickblox sample chat

-keep class com.quickblox.auth.parsers.** { *; }
-keep class com.quickblox.auth.model.** { *; }
-keep class com.quickblox.core.parser.** { *; }
-keep class com.quickblox.core.model.** { *; }
-keep class com.quickblox.core.server.** { *; }
-keep class com.quickblox.core.rest.** { *; }
-keep class com.quickblox.core.error.** { *; }
-keep class com.quickblox.core.Query { *; }

-keep class com.quickblox.users.parsers.** { *; }
-keep class com.quickblox.users.model.** { *; }

-keep class com.quickblox.chat.parser.** { *; }
-keep class com.quickblox.chat.model.** { *; }

-keep class com.quickblox.messages.parsers.** { *; }
-keep class com.quickblox.messages.model.** { *; }

-keep class com.quickblox.content.parsers.** { *; }
-keep class com.quickblox.content.model.** { *; }

-keep class org.jivesoftware.** { *; }

#sample chat
-keep class android.support.v7.** { *; }
-keep class com.bumptech.** { *; }

-dontwarn org.jivesoftware.smackx.**
-dontwarn android.support.v4.app.**
-ignorewarnings
-keep class * {
    public private *;
}



-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}