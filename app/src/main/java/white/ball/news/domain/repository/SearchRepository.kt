package white.ball.news.domain.repository

import white.ball.news.domain.model.Article

interface SearchRepository {

    fun getAllArticlesOnRequest(responseTitle: String, articles: List<Article>): List<Article>

}