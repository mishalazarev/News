package white.ball.news.presentation.detail_screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import white.ball.news.domain.model.Article
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.ball.news.R
import white.ball.news.data.api.ApiService
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.domain.util.RenderUtil
import white.ball.news.domain.util.TextUtil
import white.ball.news.presentation.ui.theme.AdditionalInformationColor


@Composable
fun DetailArticleScreen(
    clickArticle: Article,
    roomRepository: RoomRepository,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    val renderUtil = RenderUtil()
    val coroutineScope = rememberCoroutineScope()

    // Состояния для статьи и других данных
    val clickArticleState = remember { mutableStateOf(clickArticle) }
    val articlesInBookmarksState = remember { mutableStateOf<List<Article>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            articlesInBookmarksState.value = roomRepository.getArticlesInBookmarks()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        AsyncImage(
            model = clickArticleState.value.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = clickArticleState.value.sourceName,
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.width(200.dp)
                )

                Text(
                    text = clickArticleState.value.author.ifEmpty { "Unknown author" },
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    modifier = Modifier.width(200.dp)
                )

                Text(
                    text = clickArticleState.value.publishedAt,
                    style = TextStyle(
                        color = AdditionalInformationColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Row {
                Image(
                    painter = painterResource(
                        renderUtil.loadArticleIconBookmark(
                            clickArticleState.value,
                            articlesInBookmarksState.value
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(35.dp)
                        .clickable {
                            // Обработка статей в закладках
                            clickArticleState.value =
                                clickArticleState.value.copy(isInYourBookmark = !clickArticleState.value.isInYourBookmark)
                            coroutineScope.launch(Dispatchers.IO) {
                                if (clickArticleState.value.isInYourBookmark) {
                                    roomRepository.putNewArticle(clickArticleState.value)
                                } else {
                                    roomRepository.deleteArticle(clickArticleState.value)
                                }
                            }
                        }
                )

                // Иконка "поделиться"
                Image(
                    painter = painterResource(R.drawable.icon_share),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            val openShareApp = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, clickArticleState.value.url)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(openShareApp, null))
                        }
                )
            }
        }

        // Контент статьи
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 10.dp)
        ) {
            Text(
                text = clickArticleState.value.title,
                style = TextStyle(
                    color = Color.Black,
                    textAlign = TextAlign.Justify,
                    fontSize = 26.sp,
                ),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = clickArticleState.value.content.split("[").first(),
                style = TextStyle(
                    color = Color.Black,
                    textAlign = TextAlign.Justify,
                    fontSize = 16.sp,
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "more...",
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 16.sp
                ),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            val snackbarAction = snackbarHostState.showSnackbar(
                                message = "Are you sure you want to follow the link?",
                                actionLabel = "Yes"
                            )

                            if (snackbarAction == SnackbarResult.ActionPerformed) {
                                val openURL = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(clickArticleState.value.url)
                                }
                                context.startActivity(openURL)
                            }
                        }
                    }
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}