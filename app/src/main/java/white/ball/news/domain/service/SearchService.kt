package white.ball.news.domain.service

import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.SearchRepository

class SearchService : SearchRepository {

    override fun getAllArticlesOnRequest(
        responseTitle: String,
        articles: List<Article>
    ): List<Article> {
        val keyWordsRequest = responseTitle.split(" ").map { it.lowercase() }.toSet()

        return articles.filter { currentArticle ->
            val articleKeyWords = currentArticle.title.split(" ").map { it.lowercase() }.toSet()
            keyWordsRequest.any { it in articleKeyWords }
        }.sortedByDescending { currentArticle ->
            keyWordsRequest.count { currentArticle.title.lowercase().contains(it) }
        }
    }
}
