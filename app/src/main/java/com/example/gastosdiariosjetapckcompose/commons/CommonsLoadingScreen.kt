package com.example.gastosdiariosjetapckcompose.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CommonsLoadingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            Modifier
                .size(30.dp)
                .align(Alignment.Center)
        )
    }
}