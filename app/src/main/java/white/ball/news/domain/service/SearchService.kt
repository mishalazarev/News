package white.ball.news.domain.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import white.ball.news.domain.model.Article
import white.ball.news.domain.repository.SearchRepository

class SearchService : SearchRepository {

    override fun getAllArticlesOnRequest(
        responseTitle: String,
        articles: LiveData<List<Article>>
    ): List<Article> {
        val keyWordsRequest = responseTitle.split(" ").map { it.lowercase() }.toSet()

        return articles.value?.filter { currentArticle ->
            val articleKeyWords = currentArticle.title.split(" ").map { it.lowercase() }.toSet()
            keyWordsRequest.any { it in articleKeyWords }
        }?.sortedByDescending { currentArticle ->
            keyWordsRequest.count { currentArticle.title.lowercase().contains(it) }
        } ?: mutableListOf()
    }
}
