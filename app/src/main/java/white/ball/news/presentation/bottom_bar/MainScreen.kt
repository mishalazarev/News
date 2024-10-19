package white.ball.news.presentation.bottom_bar

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import white.ball.news.data.api.TAG
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.domain.util.RenderUtil
import white.ball.news.presentation.ui.theme.AdditionalInformationColor
import white.ball.news.presentation.ui.theme.ArticleBlockColor
import white.ball.news.presentation.ui.theme.MainColor

@Composable
fun MainScreen(
    clickArticle: (Article) -> Unit,
    articles: MutableState<List<Article>>,
    roomRepository: RoomRepository,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val articlesInBookmarksState = remember { mutableStateOf<List<Article>>(emptyList()) }

    roomRepository.loadDataBase(context)

    LaunchedEffect(Unit) {
        val articlesInBookmark = roomRepository.getArticlesInBookmarks()
        articlesInBookmarksState.value = articlesInBookmark

        val updatedArticles = articles.value.map { article ->
            val bookmarkArticle = articlesInBookmarksState.value.find { it.title == article.title }
            bookmarkArticle ?: article
        }
        articles.value = updatedArticles
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ArticleBlockColor)
                        .clickable {
                            clickArticle.invoke(articles.value[0])
                        },
                ) {
                    AsyncImage(
                        model = if (articles.value.isNotEmpty()) {
                            articles.value[0].urlToImage
                        } else {
                            R.drawable.no_image
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.no_image),
                    )

                    Text(
                        text = if (articles.value.isNotEmpty()) {
                            articles.value[0].title
                        } else  {
                            "No title"
                        },
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (articles.value.isNotEmpty()) {
                                articles.value[0].sourceName
                            } else {
                                "No source"
                            },
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MainColor,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Medium,
                            ),
                            modifier = Modifier
                                .widthIn(max = 50.dp)
                                .padding(start = 10.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        Text(
                            text = if (articles.value.isNotEmpty()) {
                                articles.value[0].publishedAt
                            } else {
                                "No date"
                            },
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .padding(start = 10.dp),
                        )
                    }
                }
            }

            items(
                if (articles.value.isEmpty()){
                    0
                } else {
                    articles.value.size - 1
                }) { index ->
                val currentArticle: MutableState<Article> = remember { mutableStateOf(articles.value[index + 1]) }

                if (currentArticle.value.title != "[Removed]" && currentArticle.value.urlToImage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .padding(top = 10.dp)
                            .clickable {
                                clickArticle(currentArticle.value)
                            },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = if (articles.value.isNotEmpty()) {
                                    currentArticle.value.urlToImage
                                } else {
                                    R.drawable.no_image
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(width = 120.dp, height = 70.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.no_image)
                            )

                            Column {
                                Text(
                                    text = currentArticle.value.title,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                    )
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = currentArticle.value.sourceName,
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
                                            text = currentArticle.value.publishedAt,
                                            style = TextStyle(
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            ),
                                            modifier = Modifier
                                                .padding(start = 10.dp),
                                        )
                                    }
                                    Image(
                                        painter = if (articles.value[index + 1].isInYourBookmark) {
                                            painterResource(R.drawable.icon_remove_bookmark_default)
                                        } else  {
                                            painterResource(R.drawable.icon_add_bookmark_default)
                                        },
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(end = 20.dp, bottom = 5.dp)
                                            .size(25.dp)
                                            .clickable {
                                                currentArticle.value.isInYourBookmark = !currentArticle.value.isInYourBookmark
                                                if (currentArticle.value.isInYourBookmark) {
                                                    coroutineScope.launch(Dispatchers.IO) {
                                                        roomRepository.putNewArticle(currentArticle.value)
                                                    }
                                                } else {
                                                    coroutineScope.launch(Dispatchers.IO) {
                                                        roomRepository.deleteArticle(currentArticle.value)
                                                    }
                                                }
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
}
