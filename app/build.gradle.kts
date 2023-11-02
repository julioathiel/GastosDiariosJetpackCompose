import org.jetbrains.kotlin.kapt3.base.Kapt
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //necesarios para el dagger hilt
    kotlin("kapt")//a traves de anotaciones nos permite crear codigo por detras(nos autogenera el codigo)
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.gastosdiariosjetapckcompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gastosdiariosjetapckcompose"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    //dependencia que usa ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //liveData
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //convertidor retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //agrega los iconos de material design
    implementation("androidx.compose.material:material-icons-extended")
    //libreria para navegar entre activitys
    implementation("androidx.navigation:navigation-compose:2.7.4")//se encarga del Toodo el tema de navegacion
    //material design 2
    implementation("androidx.compose.material:material")
    //libreria de datepickerdialog
    //implementation ("androidx.compose.material3:material3:1.2.0-alpha09")
    implementation("androidx.compose.material:material:1.5.3")
    //fragment
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    //data base ROOM
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
     //Dagger Hilt
    val hilt_version = "2.45"
    implementation("com.google.dagger:hilt-android:$hilt_version")// Versión actualizada de Dagger Hilt para Android
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")// Anotación de procesador de Dagger Hilt

    //para testear la UI
    val compose_ui_version = "1.5.4"
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:$compose_ui_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_ui_version")

    
}
kapt{
    correctErrorTypes = true
}
