package white.ball.news.presentation.bottom_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import white.ball.news.domain.model.Article
import white.ball.news.presentation.ui.component.BookmarkCard
import white.ball.news.presentation.view_model.BookmarksViewModel


@Composable
fun BookmarksScreen(
    bookmarksViewModel: BookmarksViewModel,
    clickArticle: (Article) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(bookmarksViewModel.articlesInBookmarks) { article ->
                BookmarkCard(
                    article = article,
                    onClick = { clickArticle(article) }
                )
            }
        }
    }
}
