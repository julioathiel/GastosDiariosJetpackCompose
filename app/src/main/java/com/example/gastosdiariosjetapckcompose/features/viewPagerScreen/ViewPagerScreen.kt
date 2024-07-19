package com.example.gastosdiariosjetapckcompose.features.viewPagerScreen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.gastosdiariosjetapckcompose.domain.model.view
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.components.BottonIndicators
import com.example.gastosdiariosjetapckcompose.features.viewPagerScreen.components.Content
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPagerScreen(navController: NavHostController, viewModel: ViewPagerViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState()

        HorizontalPager(count = view.size, state = pagerState) { page ->
            val item = view[page]
            Content(item, Modifier.align(Alignment.Center))
        }

        BottonIndicators(
            Modifier.align(Alignment.BottomCenter),
            pagerState,
            navController,
            viewModel
        )

    }
}

@Composable
fun LoaderData(modifier: Modifier, image: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(image))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}




