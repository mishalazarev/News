package white.ball.news.presentation.detail_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import white.ball.news.domain.model.Article
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
import white.ball.news.domain.repository.RoomRepository
import white.ball.news.presentation.ui.theme.AdditionalInformationColor
import white.ball.news.presentation.view_model.BookmarksViewModel


@Composable
fun DetailArticleScreen(
    clickArticle: Article,
    roomRepository: RoomRepository,
    snackbarHostState: SnackbarHostState,
    bookmarksViewModel: BookmarksViewModel,
    context: Context,
) {
    val coroutineScope = rememberCoroutineScope()
    val isSubscribed = remember { mutableStateOf(clickArticle.isInYourBookmark) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        AsyncImage(
            model = clickArticle.urlToImage,
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
                    text = clickArticle.sourceName,
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.width(200.dp)
                )

                Text(
                    text = clickArticle.author.ifEmpty { "Unknown author" },
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    modifier = Modifier.width(200.dp)
                )

                Text(
                    text = clickArticle.publishedAt,
                    style = TextStyle(
                        color = AdditionalInformationColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Row {
                Crossfade(
                    targetState = isSubscribed.value,
                    label = "",
                    animationSpec = tween(800)) { isBookmarked ->
                    if (isBookmarked) {
                        Image(
                            painter = painterResource(R.drawable.icon_remove_bookmark_default),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(35.dp)
                                .clickable {
                                    clickArticle.isInYourBookmark = false
                                    isSubscribed.value = clickArticle.isInYourBookmark
                                    coroutineScope.launch(Dispatchers.IO) {
                                        bookmarksViewModel.removeBookmark(clickArticle)
                                    }
                            }
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_add_bookmark_default),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(35.dp)
                                .clickable {
                                    clickArticle.isInYourBookmark = true
                                    isSubscribed.value = clickArticle.isInYourBookmark

                                    coroutineScope.launch(Dispatchers.IO) {
                                        bookmarksViewModel.addBookmark(clickArticle)
                                     }
                                }
                        )
                    }
                }

                // Иконка "поделиться"
                Image(
                    painter = painterResource(R.drawable.icon_share),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            val openShareApp = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, clickArticle.url)
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
                text = clickArticle.title,
                style = TextStyle(
                    color = Color.Black,
                    textAlign = TextAlign.Justify,
                    fontSize = 26.sp,
                ),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = clickArticle.content.split("[").first(),
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
                                    data = Uri.parse(clickArticle.url)
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