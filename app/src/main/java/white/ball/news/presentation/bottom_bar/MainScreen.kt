package white.ball.news.presentation.bottom_bar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.ball.news.domain.model.Article
import white.ball.news.presentation.ui.component.ArticleCard
import white.ball.news.presentation.ui.component.MainArticleCard
import white.ball.news.presentation.view_model.MainScreenViewModel
import white.ball.news.presentation.view_model.view_model_factory.rememberViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    onBookmarkClick: (Article) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val mainScreenViewModel = rememberViewModel {
        MainScreenViewModel(
            it.roomService,
            it.articlesAPI,
            it.articlesInBookmarks,
        )
    }

    coroutineScope.launch (Dispatchers.IO) {
        mainScreenViewModel.updateArticlesWithBookmarks()
    }

    // Основная структура экрана
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val articleAPI = mainScreenViewModel.mArticlesAPI.value!!
        item {
            MainArticleCard(
                onBookmarkClick = onBookmarkClick,
                article = if (articleAPI.isNotEmpty()) articleAPI[0] else null
            )
        }

        items (
            if (articleAPI.isEmpty()) {
                0
            } else {
                articleAPI.size - 1 }) { index ->
            val currentArticle = articleAPI[index + 1]

            ArticleCard(
                article = currentArticle,
                onBookmarkClick = onBookmarkClick,
                bookmarksViewModel = mainScreenViewModel,
                )
        }
    }
}