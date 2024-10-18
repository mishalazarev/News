package white.ball.news.presentation.nav_controller

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import white.ball.news.data.api.ApiService
import white.ball.news.data.storage.service.RoomService
import white.ball.news.domain.model.Article
import white.ball.news.domain.model.ItemBottomBar
import white.ball.news.domain.util.TextUtil
import white.ball.news.presentation.bottom_bar.BookmarksScreen
import white.ball.news.presentation.bottom_bar.MainScreen
import white.ball.news.presentation.bottom_bar.SearchScreen
import white.ball.news.presentation.detail_screen.DetailArticleScreen
import white.ball.news.presentation.ui.InternetNotWorking

@Composable
fun MainNavController(
    context: Context,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    articles: MutableState<List<Article>>,
    apiService: ApiService,
    roomService: RoomService,
) {
    val textUtil = TextUtil()
    val listenerClickArticle = remember { mutableStateOf<Article?>(null) }
    val clickArticleHandler: (Article) -> Unit = { clickArticle ->
        listenerClickArticle.value = clickArticle
        navController.navigate("detail_article_bookmark")
    }

    NavHost(
        navController = navController,
        startDestination = ItemBottomBar.MainScreen.route
    ) {
        composable(route = ItemBottomBar.MainScreen.route) {
            MainScreen(
                clickArticle = clickArticleHandler,
                articles = articles,
                roomRepository = roomService,
                context = context
            )
        }

        composable(route = "internet_not_working_screen") {
            InternetNotWorking(
                getArticlesFromApi = {
                    apiService.getArticles(
                        articles,
                        context,
                        textUtil.getTodayDayForAPI()
                    )
                    navController.navigate(ItemBottomBar.MainScreen.route)
                },
                context
            )
        }

        composable(route = ItemBottomBar.BookmarksScreen.route) {
            BookmarksScreen(
                clickArticle = clickArticleHandler,
                roomService
            )
        }

        composable(route = ItemBottomBar.SearchScreen.route) {
            SearchScreen(
                articles = articles,
                clickArticle = clickArticleHandler,
                roomRepository = roomService,
                context = context
            )
        }

        composable(route = "detail_article_bookmark") {
            DetailArticleScreen(
                clickArticle = checkNotNull(listenerClickArticle.value),
                roomRepository = roomService,
                snackbarHostState = snackbarHostState,
                context = context
            )
        }
    }
}