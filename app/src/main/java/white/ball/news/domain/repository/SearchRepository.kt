package white.ball.news.domain.repository

import androidx.lifecycle.LiveData
import white.ball.news.domain.model.Article

interface SearchRepository {

    fun getAllArticlesOnRequest(responseTitle: String, articles: LiveData<List<Article>>): List<Article>

}