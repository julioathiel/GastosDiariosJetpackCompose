package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home.components_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.CardInfoModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes

@Composable
fun ItemCard(
    cardInfo: CardInfoModel,
    modifier: Modifier,
    navController: NavController,
    index: Int,
) {
    Card(
        onClick = {
            when (index) {
                0 -> navController.navigate(Routes.RegistroTransaccionesScreen.route)
                else -> {}
            }
        }, modifier = modifier
            .height(120.dp)
            .padding(horizontal = 5.dp), colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.bodyCard)
        )
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = painterResource(id = cardInfo.icon),
                contentDescription = cardInfo.title,
                colorFilter = ColorFilter.tint(cardInfo.color)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                cardInfo.title,
                color = colorResource(id = R.color.tituloBlanco),
                fontWeight = FontWeight.Light
            )
        }
    }
}