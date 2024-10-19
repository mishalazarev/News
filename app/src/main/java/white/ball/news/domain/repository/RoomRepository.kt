package white.ball.news.domain.repository

import android.content.Context
import white.ball.news.data.storage.room.ArticleDao
import white.ball.news.domain.model.Article

interface RoomRepository {

    fun loadDataBase(context: Context)

    fun getArticleDao(): ArticleDao

    fun addBookmark(newArticle: Article)
    suspend fun getArticlesInBookmarks(): List<Article>
    suspend fun removeBookmark(deletedArticle: Article)

}