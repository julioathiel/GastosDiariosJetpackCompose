package com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.commons.CommonsButton
import com.example.gastosdiariosjetapckcompose.commons.CommonsLoaderData
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.mis_ui_screen.configuration.ConfigurationViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes

@Composable
fun CongratulationsScreen(navController: NavController, viewModel: ConfigurationViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.size(100.dp))
        CommonsLoaderData(
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp),
            image = R.raw.congratulation_lottie,
            repeat = false
        )
        Text(text = "Felicitaciones", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Reinicio exitoso!!")
        Spacer(modifier = Modifier.weight(1f))
        CommonsButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            title = "Ir a inicio") {
            viewModel.setResetComplete(false)
            navController.navigate(Routes.HomeScreen.route)
        }
    }
}