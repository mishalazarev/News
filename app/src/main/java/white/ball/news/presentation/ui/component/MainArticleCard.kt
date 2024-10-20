package white.ball.news.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import white.ball.news.R
import white.ball.news.domain.model.Article
import white.ball.news.presentation.ui.theme.ArticleBlockColor
import white.ball.news.presentation.ui.theme.MainColor

@Composable
fun MainArticleCard(
    onBookmarkClick: (Article) -> Unit,
    article: Article?
) {
    article?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ArticleBlockColor)
                .clickable { it.let { onBookmarkClick.invoke(it) } },
        ) {
            AsyncImage(
                model = it.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.no_image),

            )
            Text(
                text = it.title,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        top = 10.dp
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.sourceName,
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
                    text = it.publishedAt,
                    style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
    }
}