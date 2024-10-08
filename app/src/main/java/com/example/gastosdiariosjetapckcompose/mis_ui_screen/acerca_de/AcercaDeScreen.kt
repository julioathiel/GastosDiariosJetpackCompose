package com.example.gastosdiariosjetapckcompose.mis_ui_screen.acerca_de

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.gastosdiariosjetapckcompose.R

@Composable
fun AcercaDeScreen(navController: NavHostController, acercaDeViewModel: AcercaDeViewModel) {
    val applicationContext = LocalContext.current
    var versionName = ""
    val pInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
    val appIcon: Drawable =
        applicationContext.packageManager.getApplicationIcon(applicationContext.packageName)
    val title = pInfo.applicationInfo.loadLabel(applicationContext.packageManager).toString()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(model = appIcon),
            contentDescription = "App Icon",
            modifier = Modifier.size(60.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
        )
        Spacer(modifier = Modifier.height(16.dp))

        try {
            versionName = pInfo.versionName
            // Toast.makeText(applicationContext, "Versión: $versionName",Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        Text(
            text = "Versión: $versionName",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Última actualización: 10 de Julio de 2024",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { /* Acción al hacer clic */ }) {
            Text(text = "Ver cambios y novedades de la última versión")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Otros elementos como créditos, contactos, políticas de privacidad, etc.
    }
}