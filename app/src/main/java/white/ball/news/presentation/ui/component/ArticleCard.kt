package white.ball.news.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import white.ball.news.domain.model.Article
import white.ball.news.presentation.bottom_bar.ArticleDetailRow
import white.ball.news.presentation.view_model.BookmarksViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import white.ball.news.R


@Composable
fun ArticleCard(
    article: Article,
    onBookmarkClick: (Article) -> Unit,
    bookmarksViewModel: BookmarksViewModel,
) {
    val searchTextState by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(top = 10.dp)
            .clickable { onBookmarkClick(article) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            article.urlToImage?.let {
                AsyncImage(
                    model = it.ifEmpty { R.drawable.no_image },
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(width = 120.dp, height = 70.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.no_image)
            ) }

            Column {
                Text(
                    text = highlightKeywords(article.title, searchTextState),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                )

                ArticleDetailRow(article, bookmarksViewModel)
            }
        }
    }
}