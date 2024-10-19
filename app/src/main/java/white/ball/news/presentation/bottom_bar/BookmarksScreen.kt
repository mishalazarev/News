package white.ball.news.presentation.bottom_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import white.ball.news.R
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository


@Composable
fun BookmarksScreen(
    clickArticle: (Article) -> Unit,
    roomRepository: RoomRepository
) {
    val articlesInBookmarksState = remember { mutableStateOf<List<Article>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val articles = roomRepository.getArticlesInBookmarks()
            articlesInBookmarksState.value = articles
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(articlesInBookmarksState.value.size) { index ->
                val currentArticleInBookmark = articlesInBookmarksState.value[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(top = 10.dp)
                        .clickable {
                            clickArticle.invoke(currentArticleInBookmark)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = currentArticleInBookmark.urlToImage,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .size(width = 150.dp, height = 70.dp),
                            error = painterResource(R.drawable.no_image)
                        )
                        Column {
                            Text(
                                text = currentArticleInBookmark.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                ),
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                            )
                            Text(
                                text = currentArticleInBookmark.author,
                                style = TextStyle(
                                    color = Color.Black,
                                    textAlign = TextAlign.Justify,
                                    fontSize = 12.sp,
                                ),
                                modifier = Modifier
                                    .width(200.dp)
                                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}