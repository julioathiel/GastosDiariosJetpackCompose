package com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gastosdiariosjetapckcompose.domain.model.ViewPagerModel
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.LoaderData

@Composable
fun Content(item: ViewPagerModel, modifier: Modifier) {

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LoaderData(
                modifier = Modifier
                    .size(200.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                image = item.image
            )
            Text(
                item.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                item.descripcion,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}