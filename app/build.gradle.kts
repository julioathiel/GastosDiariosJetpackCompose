plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //necesarios para el dagger hilt
    kotlin("kapt")//a traves de anotaciones nos permite crear codigo por detras(nos autogenera el codigo)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")  //para habilitar ksp que es version mejorada de kapt
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
                arguments["room.incremental"] = "true"
            }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    ksp {
        //importante para la localizacion y generar migraciones automaticas
        arg("room.schemaLocation", "$projectDir/schemas")
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    //dependencia que usa ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.benchmark:benchmark-macro:1.2.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //liveData
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //convertidor retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //agrega los iconos de material design
    implementation("androidx.compose.material:material-icons-extended")
    //libreria para navegar entre activitys
    implementation("androidx.navigation:navigation-compose:2.7.7")//se encarga del Toodo el tema de navegacion
    //material design 2
    implementation("androidx.compose.material:material")
    //libreria de datepickerdialog y botonsheet
    implementation ("androidx.compose.material3:material3:1.3.0-alpha06")
    implementation("androidx.compose.material:material:1.5.3")
    //fragment
    implementation("androidx.fragment:fragment-ktx:1.8.1")
    //dataStore reeemplazo de sharedPreferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //data base ROOM
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    //ksp
    ksp("androidx.room:room-compiler:$roomVersion")
    //room ktx
    implementation("androidx.room:room-ktx:$roomVersion")
     //Dagger Hilt
    val hilt_version = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")// Versión actualizada de Dagger Hilt para Android
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")// Anotación de procesador de Dagger Hilt

    //para testear la UI
    val compose_ui_version = "1.6.8"
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:$compose_ui_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_ui_version")

    implementation("io.coil-kt:coil-compose:2.5.0")

    //viewPager
    val pager_version = "0.28.0"
    implementation("com.google.accompanist:accompanist-pager:$pager_version")
    implementation("com.google.accompanist:accompanist-pager-indicators:$pager_version")

    //lottie
    implementation ("com.airbnb.android:lottie-compose:5.2.0")

    //in-app updates
    val in_app_update_version = "2.1.0"
    implementation("com.google.android.play:app-update:$in_app_update_version")
    implementation("com.google.android.play:app-update-ktx:$in_app_update_version")


}