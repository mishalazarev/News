package white.ball.news.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import white.ball.news.presentation.ui.MainActivityContent
import white.ball.news.presentation.ui.theme.NewsTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        // Установка ориентации экрана
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            NewsTheme {
                // Состояние списка статей
                val navController = rememberNavController()
                val snackBarController = remember { SnackbarHostState() }

                // Вызов основного контента
                MainActivityContent(
                    navController = navController,
                    snackbarController = snackBarController,
                )
            }
        }
    }
}