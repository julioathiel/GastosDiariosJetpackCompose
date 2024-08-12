package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toIcon
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.HomeViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun Header(usuario: String, viewModel: HomeViewModel) {
    val url = "https://cdn.pixabay.com/photo/2016/08/24/14/29/earth-1617121_960_720.jpg"

    val uri = viewModel.selectedImageUri.value

    val singleImagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.guardarRutaImagen(ImagenSeleccionadaModel(uri = uri.toString()))
        }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                //galleryLauncher.launch("image/*")
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        SubcomposeAsyncImage(
            model = url, contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
                .align(alignment = Alignment.CenterVertically),
            contentScale = ContentScale.Crop,
        ) {
            when (painter.state) {
                AsyncImagePainter.State.Empty -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_persona_circle),
                        contentDescription = null
                    )
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Error -> {
                    Text(
                        text = "Error", modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(
                                Color.Red
                            )
                    )
                }

                else -> SubcomposeAsyncImageContent()
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Column(Modifier.fillMaxWidth()) {
            val context = LocalContext.current
            Text(text = getSaludoSegunHoraDelDia(context))
            //get date asi   3 oct.2023
            val date = DateFormat.getDateInstance().format(Date())
            Text(text = date.toString(), fontSize = 12.sp)
        }
    }
}

fun getSaludoSegunHoraDelDia(context: Context): String {
    val calendar = Calendar.getInstance()
    val hora = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hora) {
        in 6..11 -> context.getString(R.string.buenos_dias)
        in 12..19 -> context.getString(R.string.buenas_tardes)
        else -> context.getString(R.string.buenas_noches)
    }
}