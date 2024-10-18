package white.ball.news.presentation.ui

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import white.ball.news.data.api.ApiService
import white.ball.news.data.storage.service.RoomService
import white.ball.news.domain.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent(
    articles: MutableState<List<Article>>,
    isLoading: MutableState<Boolean>,
    roomService: RoomService,
    navController: NavHostController,
    snackbarController: SnackbarHostState,
    apiService: ApiService,
    context: Context,
) {
    // Отображение загрузки контента
    MainBottomAppBar(
        articles = articles,
        navController = navController,
        isLoading = isLoading,
        snackbarHostState = snackbarController,
        roomService = roomService,
        apiService = apiService,
        context = context,
    )
}
