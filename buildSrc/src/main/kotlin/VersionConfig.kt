object Android{
    val compileSdkVersion = 30
    val minSdkVersion = 21
    val targetSdkVersion = 30
    val buildToolsVersion = "30.0.3"
    val kotlinVersion = "1.4.0"
    val gradleVersion = "4.0.0"
}

object Dependencies{
    val bugly = "com.tencent.bugly:crashreport:3.2.1"
    val eventBus =  "org.greenrobot:eventbus:3.1.1"
    val fastJson = "com.alibaba:fastjson:1.2.49"
    val glide = "com.github.bumptech.glide:glide:4.9.0"
    val glide_transformations = "jp.wasabeef:glide-transformations:4.0.1"
    val arouter = "com.alibaba:arouter-api:1.5.2"
    val arouter_compiler = "com.alibaba:arouter-compiler:1.5.2"
    val permissionx = "com.permissionx.guolindev:permissionx:1.1.1"
    val location = "com.tencent.map.geolocation:TencentLocationSdk-openplatform:7.2.6"
}

object AndroidSupport{
    val appcompat =  "androidx.appcompat:appcompat:1.2.0"
    val material = "com.google.android.material:material:1.3.0"
    val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    val recyclerview =  "androidx.recyclerview:recyclerview:1.1.0"
}

object RxJava2{
    val rxjava2 = "io.reactivex.rxjava2:rxjava:2.2.10"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    val rxbinding = "com.jakewharton.rxbinding3:rxbinding:3.0.0-alpha2"
}

object NetWork{
    val okhttp = "com.squareup.okhttp3:okhttp:3.9.1"
    val retrofit = "com.squareup.retrofit2:retrofit:2.4.0"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:2.4.0"
    val retrofit_rxjava2 =  "com.squareup.retrofit2:adapter-rxjava2:2.4.0"
    val retrofit_scalars =  "com.squareup.retrofit2:converter-scalars:2.4.0"
}

object View{
    val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    val skeleton = "com.ethanhua:skeleton:1.1.2"
    val circleimageview =  "de.hdodenhof:circleimageview:2.1.0"
    val photoView =  "com.github.chrisbanes:PhotoView:2.0.0"
    val smartRefreshLayout = "com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-alpha-31"
    val shimmerlayout = "io.supercharge:shimmerlayout:2.1.0"
    val flexbox = "com.google.android:flexbox:1.0.0"
    val BRVAH = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"
    // 分组RecyclerView
    val GroupedRecyclerViewAdapter =  "com.github.donkingliang:GroupedRecyclerViewAdapter:2.4.0"
    val banner = "com.youth.banner:banner:2.1.0"
}

object WebView{
    val agentweb = "com.just.agentweb:agentweb:4.1.2"
    val tbs =  "com.tencent.tbs.tbssdk:sdk:43903"
}

object KT{
    val ktx = "androidx.core:core-ktx:1.6.0"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Android.kotlinVersion}"
    val kt_stdlib_jdk7 ="org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Android.kotlinVersion}"
}

object Jetpack{
    val paging3 = "androidx.paging:paging-runtime:3.0.0"
    val paging3_rxjava = "androidx.paging:paging-rxjava2:3.0.0" // optional
    val room = "androidx.room:room-runtime:2.2.0"
    val room_compiler = "androidx.room:room-compiler:2.2.0"
    val room_ktx = "androidx.room:room-ktx:2.2.0"
    val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"

}

object SkinSupport{
    val skin = "skin.support:skin-support:4.0.5"                // skin-support
    val skin_appcompat =  "skin.support:skin-support-appcompat:4.0.5"       // skin-support 基础控件支持
    val skin_design =  "skin.support:skin-support-design:4.0.5"            // skin-support-design material design 控件支持[可选]
    val skin_cardview =  "skin.support:skin-support-cardview:4.0.5"          // skin-support-cardview CardView 控件支持[可选]
    val skin_constraint =  "skin.support:skin-support-constraint-layout:4.0.5" // skin-support-constraint-layout ConstraintLayout 控件支持[可选]
}