package white.ball.news.presentation.bottom_bar

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.ball.news.R
import white.ball.news.domain.model.Article
import white.ball.news.domain.service.SearchService
import white.ball.news.presentation.ui.theme.MainColor
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.presentation.ui.component.ArticleCard
import white.ball.news.presentation.ui.component.SearchTextField
import white.ball.news.presentation.view_model.BookmarksViewModel

@Composable
fun SearchScreen(
    articles: MutableState<List<Article>>,
    bookmarksViewModel: BookmarksViewModel,
    onBookmarkClick: (Article) -> Unit,
    roomRepository: RoomRepository,
    context: Context
) {
    // Состояния для текстового поля поиска
    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }
    val articlesResponse = remember { mutableStateOf(listOf<Article>()) }
    val coroutineScope = rememberCoroutineScope()
    val searchService = SearchService()


    roomRepository.loadDataBase(context)

    // Обновление закладок в статьях
    LaunchedEffect(Unit) {
        val updatedArticles = articles.value.map { article ->
            val bookmarkArticle = bookmarksViewModel.articlesInBookmarks.find { it.title == article.title }
            bookmarkArticle ?: article
        }
        articles.value = updatedArticles
    }

    // Основная структура экрана
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchTextField(
            articles = articles,
            searchTextState = searchTextState,
            articlesResponse = articlesResponse,
            searchService = searchService,
            coroutineScope = coroutineScope
        )

        SearchResultsList(
            articlesResponse = articlesResponse,
            onBookmarkClick = onBookmarkClick,
            bookmarksViewModel = bookmarksViewModel,
        )
    }
}

// результат поиска
@Composable
fun SearchResultsList(
    articlesResponse: MutableState<List<Article>>,
    onBookmarkClick: (Article) -> Unit,
    bookmarksViewModel: BookmarksViewModel,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        items(articlesResponse.value) { currentArticle ->
            ArticleCard(
                article = currentArticle,
                onBookmarkClick = onBookmarkClick,
                bookmarksViewModel = bookmarksViewModel,
            )
        }
    }
}

@Composable
fun ArticleDetailRow(
    article: Article,
    bookmarksViewModel: BookmarksViewModel,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Исходное имя источника и дата публикации
        Text(
            text = article.sourceName,
            style = TextStyle(fontSize = 14.sp, color = MainColor, fontWeight = FontWeight.Medium),
            modifier = Modifier.widthIn(max = 120.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = article.publishedAt,
            style = TextStyle(fontSize = 11.sp, color = Color.Gray),
            modifier = Modifier.padding(start = 10.dp),
        )

        // Иконка закладки
        BookmarkIcon(article, bookmarksViewModel)
    }
}

@Composable
fun BookmarkIcon(
    article: Article,
    bookmarksViewModel: BookmarksViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = if (article.isInYourBookmark) {
            painterResource(R.drawable.icon_remove_bookmark_default)
        } else {
            painterResource(R.drawable.icon_add_bookmark_default)
        },
        contentDescription = null,
        modifier = Modifier
            .padding(end = 20.dp, bottom = 5.dp)
            .size(25.dp)
            .clickable {
                coroutineScope.launch(Dispatchers.IO) {
                    toggleBookmark(article, bookmarksViewModel)
                }
            }
    )
}

private suspend fun toggleBookmark(article: Article, bookmarksViewModel: BookmarksViewModel) {
    article.isInYourBookmark = !article.isInYourBookmark
    if (article.isInYourBookmark) {
        bookmarksViewModel.addBookmark(article)
    } else {
        bookmarksViewModel.removeBookmark(article)
    }
}
