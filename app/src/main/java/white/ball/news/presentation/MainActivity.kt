package white.ball.news.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import white.ball.news.data.api.ApiService
import white.ball.news.data.storage.service.RoomService
import white.ball.news.domain.model.Article
import white.ball.news.domain.util.TextUtil
import white.ball.news.presentation.ui.MainActivityContent
import white.ball.news.presentation.ui.theme.NewsTheme
import white.ball.news.presentation.view_model.BookmarksViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    // Объекты для использования в классе
    private val textUtil = TextUtil()
    private val apiService = ApiService()
    private val roomService = RoomService()
    private lateinit var bookmarksViewModel: BookmarksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация базы данных
        roomService.loadDataBase(application)

        // Инициализация ViewModel
        bookmarksViewModel = BookmarksViewModel(roomService)

        installSplashScreen()
        enableEdgeToEdge()

        // Установка ориентации экрана
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            NewsTheme {
                // Состояние списка статей, работа с
                val navController = rememberNavController()
                val snackBarController = remember { SnackbarHostState() }
                val isLoading = remember { mutableStateOf(false) }
                val articles = remember { mutableStateOf(emptyList<Article>()) }

                // Запуск получения статей
                fetchArticles(articles)

                // Вызов основного контента
                MainActivityContent(
                    articles = articles,
                    isLoading = isLoading,
                    roomService = roomService,
                    navController = navController,
                    snackbarController = snackBarController,
                    apiService = apiService,
                    context = LocalContext.current,
                    bookmarksViewModel = bookmarksViewModel
                )
            }
        }
    }

    // Функция для получения статей
    private fun fetchArticles(articles: MutableState<List<Article>>) {
        apiService.getArticles(
            articles,
            application,
            textUtil.getTodayDayForAPI()
        )
    }
}