package white.ball.news.presentation.nav_controller

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import white.ball.news.data.api.ApiService
import white.ball.news.domain.model.Article
import white.ball.news.domain.model.ItemBottomBar
import white.ball.news.domain.util.TextUtil
import white.ball.news.presentation.bottom_bar.BookmarksScreen
import white.ball.news.presentation.bottom_bar.MainScreen
import white.ball.news.presentation.bottom_bar.SearchScreen
import white.ball.news.presentation.detail_screen.DetailArticleScreen
import white.ball.news.presentation.ui.InternetNotWorking
import white.ball.news.presentation.view_model.MainScreenViewModel
import white.ball.news.presentation.view_model.view_model_factory.rememberViewModel

@Composable
fun MainNavController(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    // Утилиты для работы с текстом
    val apiService = ApiService()
    val textUtil = TextUtil()
    val context = LocalContext.current

    // Состояние для сохранения выбранной статьи
    val listenerClickArticle = remember { mutableStateOf<Article?>(null) }
    val mainViewModel = rememberViewModel {
        MainScreenViewModel(
            it.roomService,
            it.articlesAPI,
            it.articlesInBookmarks,
        )
    }

    // Обработчик нажатия на статью
    val clickArticleHandler: (Article) -> Unit = { clickedArticle ->
        listenerClickArticle.value = clickedArticle
        navController.navigate("detail_article_bookmark")
    }

    NavHost(
        navController = navController,
        startDestination = ItemBottomBar.MainScreen.route
    ) {
        // Главный экран
        composable(route = ItemBottomBar.MainScreen.route) {
            MainScreen(
                onBookmarkClick = clickArticleHandler,
            )
        }

        // Экран отсутствия интернета
        composable(route = "internet_not_working_screen") {
            InternetNotWorking(
                getArticlesFromApi = {
                    val articles = MutableLiveData<List<Article>>()
                    apiService.getArticles(
                        articlesListener = articles,
                        context = context,
                        todayDayForAPI = textUtil.getTodayDayForAPI()
                    )
                    mainViewModel.setArticlesAPI(articles.value!!.toList())
                    navController.navigate(ItemBottomBar.MainScreen.route)
                }
            )
        }

        // Экран закладок
        composable(route = ItemBottomBar.BookmarksScreen.route) {
            BookmarksScreen(
                clickArticle = clickArticleHandler,
            )
        }

        // Экран поиска статей
        composable(route = ItemBottomBar.SearchScreen.route) {
            SearchScreen(
                onBookmarkClick = clickArticleHandler,
            )
        }

        // Экран подробностей статьи из закладок
        composable(route = "detail_article_bookmark") {
            DetailArticleScreen(
                clickArticle = checkNotNull(listenerClickArticle.value) { "Selected article cannot be null" },
                snackbarHostState = snackbarHostState,
            )
        }
    }
}