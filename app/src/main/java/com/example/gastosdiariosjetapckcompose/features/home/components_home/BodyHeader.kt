package com.example.gastosdiariosjetapckcompose.features.home.components_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.features.home.HomeViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes

@Composable
fun BodyHeader(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val limitePorDiaObserve by sharedLogic.limitePorDia.collectAsState()
    val limitePorDia = sharedLogic.formattedCurrency(limitePorDiaObserve)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.tu_dinero_actual))

        TextButton(
            onClick = { navController.navigate(route = Routes.MovimientosScreen.route) },
              //border = BorderStroke(0.dp, colorResource(id = R.color.blue)), // Borde transparente
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),// Fondo transparente
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text(
                    text = stringResource(R.string.ir_a_movimientos),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_next),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

    }


    Column(modifier = Modifier.padding(start = 16.dp)) {
        //muestra el dinero actual al usuario
        FormattedCurrencyCash(homeViewModel)

        Text(
            text = stringResource(R.string.por_dia, limitePorDia),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

    }
}