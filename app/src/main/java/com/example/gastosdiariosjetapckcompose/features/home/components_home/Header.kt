package com.example.gastosdiariosjetapckcompose.features.home.components_home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun Header(usuario: String, homeViewModel: HomeViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // homeViewModel.guardarRutaImagen(ImagenSeleccionadaModel(uri = it.toString()))
        }
    }

    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .clickable { galleryLauncher.launch("image/*") }
    ) {

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Imagen seleccionada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp)
            )
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.ic_persona_circle),
                contentDescription = "imagen por defecto",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp),
                colorFilter = ColorFilter.tint(Color.LightGray)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Column(Modifier.fillMaxWidth()) {
            Text(text = obtenerSaludoSegunHoraDelDia())
            //obteniendo fecha asi   3 oct.2023
            val date = DateFormat.getDateInstance().format(Date())
            Text(text = date.toString(), fontSize = 12.sp)
        }
    }
}

fun obtenerSaludoSegunHoraDelDia(): String {
    val calendar = Calendar.getInstance()
    val hora = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hora) {
        in 6..11 -> "Buenos días"
        in 12..17 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}