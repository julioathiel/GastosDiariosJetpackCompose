package com.example.gastosdiariosjetapckcompose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF6200EE), // Morado vibrante
////onPrimary = Color.White, // Blanco para contraste
//primaryContainer = Color(0xFFE8F0FE), // Morado claro
//onPrimaryContainer= Color(0xFF200080), // Morado oscuro
////inversePrimary = Color(0xFFFFFFFF), // Blanco para contraste
//secondary =Color(0xFF03DAC5), // Verde turquesa
//onSecondary = Color.White, // Blanco para contraste
//secondaryContainer = Color(0xFFE0F2F1), // Verde claro
//onSecondaryContainer = Color(0xFF00785F), // Verde oscuro
//tertiary = Color(0xFF0088FF), // Azul vibrante
//onTertiary = Color.White, // Blanco para contraste
//tertiaryContainer = Color(0xFFE0F2FF), // Azul claro
//onTertiaryContainer = Color(0xFF005080), // Azul oscuro
//background = Color(0xFFF5F5F5), // Gris claro
//onBackground = Color(0xFF212121), // Gris oscuro
//surface = Color.White, // Blanco para contraste
//onSurface = Color(0xFF212121), // Gris oscuro
//surfaceVariant = Color(0xFFE7E7E7), // Gris claro
//onSurfaceVariant = Color(0xFF424242), // Gris oscuro
//surfaceTint = Color(0xFF6200EE), // Morado vibrante
//inverseSurface = Color(0xFF212121), // Gris oscuro
//inverseOnSurface = Color.White, // Blanco para contraste
//error = Color(0xFFB00020), // Rojo
//onError = Color.White, // Blanco para contraste
//errorContainer = Color(0xFFF9DEDC), // Rojo claro
//onErrorContainer = Color(0xFF410E0B), // Rojo oscuro
//outline = Color(0xFF757575), // Gris medio
//outlineVariant = Color(0xFFC2C2C2), // Gris claro
//scrim = Color(0xFF000000), // Negro opaco
//surfaceBright = Color(0xFFFFFFFF), // Blanco
//surfaceContainer = Color(0xFFF2F2F2), // Gris claro
//surfaceContainerHigh = Color(0xFFEEEEEE), // Gris claro
//surfaceContainerHighest = Color(0xFFE0E0E0), // Gris claro
//surfaceContainerLow = Color(0xFFF9F9F9), // Gris claro
//surfaceContainerLowest = Color(0xFFFAFAFA), // Gris claro
//surfaceDim = Color(0xFFD9D9D9), // Gris medio
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,
    primary = BlueLighthNight,
    onPrimary = BlueDarck, //texto del boton predefinnido
    primaryContainer = BlueDarck, //el floatingBotton lo usa
    secondaryContainer = BlueOscuroNight,// fondo del  item bottomBar
    background = Color.Black,
//    onSurfaceVariant = Color.Yellow, //para algunos textos onSurfaceVariant

)

private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40,
    primary = BlueOscuro,
    primaryContainer = BlueLightFloating,
    secondaryContainer = BlueLigth,

    tertiaryContainer = VerdeCard
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun GastosDiariosJetapckComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    //  El color dinámico está disponible en Android 12
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                colorScheme.background.toArgb()//si queremos cambiar la parte de arriba de todoo
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}