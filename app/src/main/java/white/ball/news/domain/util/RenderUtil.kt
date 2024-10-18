package white.ball.news.domain.util

import white.ball.news.R
import white.ball.news.domain.model.Article

class RenderUtil {

    fun loadArticleIconBookmark(currentArticle: Article,articlesInBookmark: List<Article>):Int {
        articlesInBookmark.forEach { articleInBookmark ->
            if (articleInBookmark.title == currentArticle.title) {
                currentArticle.isInYourBookmark = true
                return R.drawable.icon_remove_bookmark_default
            }
        }

        return R.drawable.icon_add_bookmark_default
    }

}