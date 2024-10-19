package white.ball.news.presentation.bottom_bar

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.ball.news.R
import white.ball.news.domain.model.Article
import white.ball.news.domain.service.SearchService
import white.ball.news.presentation.ui.theme.MainColor
import white.ball.news.presentation.ui.theme.SnackBarColor
import androidx.compose.material3.Card
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.domain.util.RenderUtil
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import white.ball.news.presentation.view_model.BookmarksViewModel

@Composable
fun SearchScreen(
    articles: MutableState<List<Article>>,
    bookmarksViewModel: BookmarksViewModel,
    clickArticle: (Article) -> Unit,
    roomRepository: RoomRepository,
    context: Context
) {
    val searchTextState = remember { mutableStateOf(TextFieldValue("")) }
    val articlesResponse = remember { mutableStateOf(listOf<Article>()) }
    val articlesInBookmarksState = remember { mutableStateOf<List<Article>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val searchService = SearchService()
    val renderUtil = RenderUtil()

    roomRepository.loadDataBase(context)

    LaunchedEffect(Unit) {
        val articlesInBookmark = roomRepository.getArticlesInBookmarks()
        articlesInBookmarksState.value = articlesInBookmark
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // состояние поиска

        TextField(
            value = searchTextState.value,
            onValueChange = { value ->
                searchTextState.value = value
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .align(Alignment.CenterHorizontally),
            textStyle = TextStyle(
                fontSize = 20.sp
            ),
            leadingIcon = {
                if (searchTextState.value.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchTextState.value = TextFieldValue("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp),
                            tint = Color.Gray
                        )
                    }
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            coroutineScope.launch {
                                articlesResponse.value = searchService.getAllArticlesOnRequest(searchTextState.value.text, articles.value)
                            }
                        }
                )
            },
            placeholder = { Text(
                text = "Search...",
                modifier = Modifier
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontSize = 20.sp
                )
            )},
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = SnackBarColor,
                cursorColor = SnackBarColor,
                backgroundColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
        ) {
            items(articlesResponse.value.size) { index ->
                val currentArticle = articlesResponse.value[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(top = 10.dp)
                        .clickable {
                            clickArticle(currentArticle)
                        },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = if (currentArticle.urlToImage == "") {
                                R.drawable.no_image
                            } else {
                                currentArticle.urlToImage
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(width = 120.dp, height = 70.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop,
                        )

                        Column {
                            Text(
                                text = highlightKeywords(currentArticle.title, searchTextState.value.text),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentArticle.sourceName,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            color = MainColor,
                                            fontStyle = FontStyle.Normal,
                                            fontWeight = FontWeight.Medium,
                                        ),
                                        modifier = Modifier
                                            .widthIn(max = 120.dp),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = currentArticle.publishedAt,
                                        style = TextStyle(
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        ),
                                        modifier = Modifier
                                            .padding(start = 10.dp),
                                    )
                                }
                                Image(
                                    painter = painterResource(
                                        renderUtil.loadArticleIconBookmark(
                                            currentArticle,
                                            articlesInBookmarksState.value
                                        )
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 20.dp, bottom = 5.dp)
                                        .size(25.dp)
                                        .clickable {
                                            currentArticle.isInYourBookmark =
                                                !currentArticle.isInYourBookmark
                                            if (currentArticle.isInYourBookmark) {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    bookmarksViewModel.addBookmark(currentArticle)
                                                }
                                            } else {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    bookmarksViewModel.removeBookmark(currentArticle)
                                                }
                                            }
                                            renderUtil.loadArticleIconBookmark(
                                                currentArticle,
                                                articlesInBookmarksState.value
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Функция для выделения ключевых слов
@Composable
fun highlightKeywords(title: String, searchText: String): AnnotatedString {
    val keywords = searchText.split(" ").filter { it.isNotEmpty() }.map { it.lowercase() }
    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        val titleLower = title.lowercase()

        while (currentIndex < titleLower.length) {
            var matched = false
            for (keyword in keywords) {
                if (titleLower.startsWith(keyword, currentIndex)) {
                    // Если найдено совпадение, добавляем его как выделенное
                    append(title.substring(currentIndex, currentIndex + keyword.length))
                    addStyle(SpanStyle(color = SnackBarColor), length - keyword.length, length)
                    currentIndex += keyword.length
                    matched = true
                    break
                }
            }
            if (!matched) {
                append(title[currentIndex].toString())
                currentIndex++
            }
        }
    }
    return annotatedString
}