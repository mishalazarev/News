package white.ball.news.presentation.bottom_bar

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.presentation.ui.component.ArticleCard
import white.ball.news.presentation.ui.component.MainArticleCard
import white.ball.news.presentation.view_model.BookmarksViewModel

@Composable
fun MainScreen(
    onBookmarkClick: (Article) -> Unit,
    articles: MutableState<List<Article>>,
    roomRepository: RoomRepository,
    bookmarksViewModel: BookmarksViewModel,
    context: Context
) {
    // Загружаем базу данных
    roomRepository.loadDataBase(context)

    LaunchedEffect(Unit) {
        val updatedArticles = articles.value.map { article ->
            val bookmarkArticle =
                bookmarksViewModel.articlesInBookmarks.find { it.title == article.title }
            bookmarkArticle ?: article
        }
        articles.value = updatedArticles
    }

    // Основная структура экрана
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MainArticleCard(
                onBookmarkClick = onBookmarkClick,
                article = if (articles.value.isNotEmpty()) articles.value[0] else null
            )
        }

        items (
            if (articles.value.isEmpty()) {
                0
            } else {
                articles.value.size - 1 }) { index ->
            val currentArticle = articles.value[index + 1]

            ArticleCard(
                article = currentArticle,
                onBookmarkClick = onBookmarkClick,
                bookmarksViewModel = bookmarksViewModel,
                )
        }
    }
}