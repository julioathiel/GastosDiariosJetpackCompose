package com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gastosdiariosjetapckcompose.domain.model.view
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.ViewPagerViewModel
import com.example.gastosdiariosjetapckcompose.navigation.Routes
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottonIndicators(
    modifier: Modifier,
    pagerState: PagerState,
    navController: NavHostController,
    viewModel: ViewPagerViewModel
) {
    val scope = rememberCoroutineScope()
    val buttonText = if (pagerState.currentPage == view.size - 1) "Skip" else "Next"
    Row(modifier.padding(bottom = 30.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            }, enabled = pagerState.currentPage > 0,
            border = BorderStroke(0.dp, color = Color.Transparent)
        ) {
            Text(text = "Atras")
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.onSurfaceVariant,
            inactiveColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Espacio para alinear el botón a la derecha
        Spacer(modifier = Modifier.weight(1f))
        //boton al lado end
        OutlinedButton(
            onClick = {
                if (pagerState.currentPage == view.size - 1) {
                    scope.launch {
                        viewModel.setShowViewPager(true)
                        navController.navigate(Routes.HomeScreen.route)
                    }
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            border = BorderStroke(0.dp, color = Color.Transparent)
        ) {
            Text(text = buttonText)
        }

    }
}