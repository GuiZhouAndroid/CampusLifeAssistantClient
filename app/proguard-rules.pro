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
#---------------------------------1.百度地图SDK-------------------------------
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-dontwarn com.baidu.**
#---------------------------------2.okgo包-------------------------------
#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}
#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}
#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}
#---------------------------------3.OkHttp-------------------------------
# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

#---------------------------------3.XUI-------------------------------
# XUI
-keep class com.xuexiang.xui.widget.edittext.materialedittext.** { *; }


# 不混淆这个包下的类
-keep class com.hjq.easy.demo.http.** {
    <fields>;
}