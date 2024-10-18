package white.ball.news.domain.repository

import android.content.Context
import white.ball.news.domain.model.Article

interface RoomRepository {

    fun loadDataBase(context: Context)

    fun putNewArticle(newArticle: Article)
    suspend fun getArticlesInBookmarks(): List<Article>
    suspend fun deleteArticle(deletedArticle: Article)

}